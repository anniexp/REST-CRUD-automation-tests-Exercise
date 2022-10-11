package Tests.TestLocationCRUD;

import Framework.BaseClass;
import com.github.cliftonlabs.json_simple.JsonObject;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static Framework.BaseClass.accessToken;
import static Framework.CRUDLocations.createLocationRequestBody;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class TestLocationGetPost {
    @BeforeTest
    public static void SetAuthToken() {
        baseURI = BaseClass.baseURI;
        accessToken = BaseClass.setAuthToken();
    }

    @Test(description = "TC19")
    public static void getAllLocationsValid() {

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON).get("/api/locations").
                then().
                //assert status code is 200 -OK
                        statusCode(200).body("page", equalTo(1)).
                log().all();
    }

    @Test(description = "TC20")
    public static void getSingleLocationValid() {
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().get("/api/locations/1").
                then().
                statusCode(200).body("id", equalTo(1)).
                log().all();

    }

    @Test(description = "TC21", testName = "Try to get a location with an id, which does not exist")
    public static void getSingleLocationNotFound() {
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().get("/api/locations/200").
                then().
                statusCode(404).body(containsString("not found")).
                log().all();
    }

    @Test(description = "TC22", testName = "Try to get a location with invalid format id")
    public static void getSingleLocationBadRequest() {

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().get("/api/locations/asvgwasegews").
                then().
                statusCode(400).
                log().all();
    }

    @Test(description = "TC23" , testName = "Create a location with valid request")
    public static void postLocationValid() {
        // valid location name which does not already exist in the db
        String name = "locationAutomatic3";

        JsonObject value = createLocationRequestBody(name);
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                body(value).when().
                post("/api/locations").then().statusCode(201).log().all();
    }

    @Test(description = "TC24", testName = "Try to create a location with typing in invalid name",
            dataProvider = "locationInvalidDataProvider"
    )
    public static void postLocationInvalidFormatInput(String name) {
        JsonObject value = createLocationRequestBody(name);
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                body(value).when().
                post("/api/locations").then().statusCode(400).
                body(containsString("Address must be between 2-100 characters!")).

                log().all();
    }

    @Test(description = "TC25" , testName = "Try to create a location with an empty request body")
    public static void postLocationEmptyBody() {
        JsonObject value = new JsonObject();
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                body(value).when().
                post("/api/locations").then().statusCode(400).
                body(containsString("Address can't be empty!")).
                log().all();
    }

    @Test(description = "TC26", testName = "Try to create a location with a name, which already exists in the system")
    public static void postLocationWithAlreadyRegisteredName() {
        //name which already exists in the db
        String name = "locationAutomatic";

        JsonObject value = createLocationRequestBody(name);
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                body(value).when().
                post("/api/locations").then().statusCode(400).body(containsString("Location with address")).body(containsString("already exists!")).log().all();
    }
    @Test(description = "TC27", testName = "Try to get all locations without an authorization token")
    public static void getAllLocationsWithoutAuthentication() {
        given().headers(

                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON).get("/api/locations").
                then().
                statusCode(403).
                log().all();
    }

    @Test(description = "TC28", testName = "Try to get a location without an authorization token")
    public static void getSingleLocationWithoutAuthentication() {
        given().headers(
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().get("/api/locations/1").
                then().
                statusCode(403).
                log().all();
    }

    @DataProvider(name = "locationInvalidDataProvider")
    public Object[][] locationInvalidDataProvider() {
        return new Object[][]
                {
                        {""},
                        {"e"},
                        {"shgggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggeggg"}

                };
    }
}
