package Tests.TestTypeCRUD;

import Framework.BaseClass;
import com.github.cliftonlabs.json_simple.JsonObject;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static Framework.BaseClass.accessToken;
import static Framework.CRUDTypes.createTypeRequestBody;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class TestTypeGetPost {

    @BeforeTest
    public static void SetAuthToken() {
        baseURI = BaseClass.baseURI;
        accessToken = BaseClass.setAuthToken();
    }

    @Test(description = "TC37", testName = "Get page 1 with list of first 10 types")
    public static void getAllTypesValid() {

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON).get("/api/types?page=1&pageSize=10").
                then().
                //assert status code is 200 -OK
                        statusCode(200).body("page", equalTo(1)).
                log().all();
    }

    @Test(description = "TC38", testName = "Get an existing type")
    public static void getSingleTypeValid() {
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().get("/api/types/1").
                then().
                statusCode(200).body("id", equalTo(1)).
                log().all();

    }

    @Test(description = "TC39", testName = "Try to get a type with an id, which does not exist")
    public static void getSingleTypeNotFound() {


        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().get("/api/types/200").
                then().
                statusCode(404).body(containsString("not found")).
                log().all();

    }

    @Test(description = "TC40", testName = "Try to get a type with invalid format id")
    public static void getSingleTypeBadRequest() {

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().get("/api/types/asvgwasegews").
                then().
                statusCode(400).
                log().all();

    }

    @Test(description = "TC41" , testName = "Create a type with valid request")
    public static void postTypeValid() {
        // valid type name which does not already exist in the db
        String name = "typeAutomatic";



        JsonObject value = createTypeRequestBody(name);
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                body(value).when().
                post("/api/types").then().statusCode(201).log().all();

    }


    @Test(description = "TC42", testName = "Try to create a type with typing in invalid name",
            dataProvider = "typeInvalidDataProvider"
    )
    public static void postTypeInvalidFormatInput(String name) {


        JsonObject value = createTypeRequestBody(name);
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                body(value).when().
                post("/api/types").then().statusCode(400).
                body(containsString("Type name should be between 2-100 characters!")).

                log().all();


    }

    @Test(description = "TC43", testName = "Try to create a type with a name, which already exists in the system")
    public static void postTypeWithAlreadyRegisteredName() {

        //name which already exists in the db
        String name = "dddfsgsj@fsgggf";

        JsonObject value = createTypeRequestBody(name);
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                body(value).when().
                post("/api/types").then().statusCode(400).body(containsString("Type with name")).body(containsString("already exists!")).log().all();

    }
    @Test(description = "TC44", testName = "Try to get all types without an authorization token")
    public static void getAllTypesWithoutAuthentication() {

        given().headers(

                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON).get("/api/types").
                then().
                statusCode(403).
                log().all();
    }

    @Test(description = "TC45", testName = "Try to get a type without an authorization token")
    public static void getSingleTypeWithoutAuthentication() {

        given().headers(
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().get("/api/types/1").
                then().
                statusCode(403).
                log().all();

    }

    @DataProvider(name = "typeInvalidDataProvider")
    public Object[][] typeInvalidDataProvider() {
        return new Object[][]
                {
                        {""},
                        {"e"},
                        {"shgggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggeggg"}

                };

    }
}
