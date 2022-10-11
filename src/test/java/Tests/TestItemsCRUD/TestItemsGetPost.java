package Tests.TestItemsCRUD;

import Framework.BaseClass;
import com.github.cliftonlabs.json_simple.JsonObject;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static Framework.BaseClass.accessToken;
import static Framework.CRUDItems.createItemRequestBody;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class TestItemsGetPost {
    @BeforeTest
    public static void SetAuthToken() {
        baseURI = BaseClass.baseURI;
        accessToken = BaseClass.setAuthToken();
    }

    @Test(description = "TC53")
    public static void getAllItemsValid() {
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON).get("/api/items").
                then().
                //assert status code is 200 -OK
                        statusCode(200).body("page", equalTo(1)).
                log().all();
    }

    @Test(description = "TC54")
    public static void getSingleItemValid() {
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().get("/api/items/1").
                then().
                statusCode(200).body("id", equalTo(1)).
                log().all();
    }

    @Test(description = "TC55", testName = "Try to get an item with an id, which does not exist")
    public static void getSingleItemNotFound() {
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().get("/api/items/200").
                then().
                statusCode(404).body(containsString("not found")).
                log().all();
    }

    @Test(description = "TC56", testName = "Try to get an item with invalid format id")
    public static void getSingleLocationBadRequest() {

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().get("/api/items/asvgwasegews").
                then().
                statusCode(400).
                log().all();
    }

    @Test(description = "TC57" , testName = "Create an item with valid request")
    public static void postItemValid() {

      String assignee = "user1@gmail.com";
        String inventoryNumber = "gsgwsgsgge";
        String location = "LocationA";
        String model = "fesgeg";
        String owner = "user1@gmail.com";
        String serialNumber = "123215";
        String type = "typeAutomaticTestPut";

        JsonObject value = createItemRequestBody( assignee, inventoryNumber, location,
                model, owner, serialNumber, type );
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                body(value).when().
                post("/api/items").then().statusCode(201).log().all();
    }


    @Test(description = "TC58", testName = "Try to create an item with typing in invalid data",
            dataProvider = "itemsInvalidDataProvider"
    )
    public static void postItemsInvalidFormatInput(String assignee,String inventoryNumber, String location,
                                                   String model,String owner,String serialNumber,String type) {
        JsonObject value = createItemRequestBody( assignee, inventoryNumber, location,
                model, owner, serialNumber, type );
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                body(value).when().
                post("/api/items").then().statusCode(400).
                body(containsString("must be between 2-100 characters!")).

                log().all();
    }
    @Test(description = "TC59" , testName = "Try to create an item with an empty request body")
    public static void postItemEmptyBody() {
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
                post("/api/items").then().statusCode(400).
                body(containsString("Owner email can't be empty!")).
                body(containsString("Inventory number can't be empty!")).
                body(containsString("Serial number can't be empty!")).
                body(containsString("Model can't be empty!")).
                body(containsString("Location can't be empty!")).
                body(containsString("Assignee email can't be empty!")).
                body(containsString("Type can't be empty!")).

                log().all();
    }


    @Test(description = "TC60", testName = "Try to get all items without an authorization token")
    public static void getAllItemsWithoutAuthentication() {
        given().headers(
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON).get("/api/items").
                then().
                statusCode(403).
                log().all();
    }

    @Test(description = "TC61", testName = "Try to get an item without an authorization token")
    public static void getSingleItemWithoutAuthentication() {
        given().headers(
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().get("/api/items/1").
                then().
                statusCode(403).
                log().all();
    }

    @DataProvider(name = "itemsInvalidDataProvider")
    public Object[][] itemsInvalidDataProvider() {
        return new Object[][]
                {
                        {"","","","","","",""},
                        {"user1@gmail.com","shggggggggggggggggbbbbbbbbbbbbggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggbnggg",
                                "LocationA","shgggggggggggggggggggggggggggggbbbbbbbbbbbbbbbgggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggbnggg",
                                "test@mail.com","shgggggggggggggggggggggbbbbbbbbbbbbbgggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggfnftftgggggggggggg",
                                "fcbjjjjjfjf"},
                        {"user1@gmail.com","f","LocationA","f","test@mail.com","f","fcbjjjjjfjf"}
                };
    }
}
