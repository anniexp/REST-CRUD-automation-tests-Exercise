package Tests.TestItemsCRUD;

import Framework.BaseClass;
import com.github.cliftonlabs.json_simple.JsonObject;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static Framework.BaseClass.accessToken;
import static Framework.CRUDItems.createItemRequestBody;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class TestPutDeleteItems {
    @BeforeTest
    public static  void SetAuthToken (){
        baseURI = BaseClass.baseURI;
        accessToken = BaseClass.setAuthToken();
    }

    @Test(description = "TC62", testName = "Edit an existing item")
    public static void putItemTestValid( ){

        String assignee = "test@mail.com";
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
                        ContentType.JSON).

                body(value).when().
                put("/api/items/1").
                then().statusCode(200).
                body("assignee.email",equalTo("test@mail.com")).
                log().all();
    }
    @Test(description = "TC63", testName = "Try to edit an not-existing item")
    public static void putLocationNotExist( ){


        String assignee = "test@mail.com";
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
                        ContentType.JSON).

                body(value).when().
                put("/api/items/1200").
                then().statusCode(404);



    }
    @Test(description = "TC64",  testName = "Try to edit an not-existing item with incorrect format id")
    public static void putTestLocationInvalidFormatInput(){
        String assignee = "test@mail.com";
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
                        ContentType.JSON).

                body(value).when().
                put("/api/items/ygyhgj").
                then().statusCode(400).
                log().all();
    }

    @Test(description = "TC65" , testName = " Delete an existing item")
    public static void deleteSingleLocationValid() {

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().delete("/api/items/10").
                then().
                statusCode(204).
                log().all();

    }

    @Test(description = "TC66", testName = "Try to delete an item with an id, who does not exist in the db" )
    public static void deleteSingleLocationNotFound() {

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().delete("/api/items/200").
                then().
                statusCode(404).body(containsString("not found")).
                log().all();

    }

    @Test(description = "TC67", testName = "Try to delete an item with incorrect format id")
    public static void deleteSingleLocationBadRequest() {

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().delete("/api/items/asvgwasegews").
                then().
                statusCode(400).
                log().all();

    }

    @Test(description = "TC68", testName = "Try to update an item without an authorization token")
    public static void putLocationWithoutAuthentication( ){

        String assignee = "test@mail.com";
        String inventoryNumber = "gsgwsgsgge";
        String location = "LocationA";
        String model = "fesgeg";
        String owner = "user1@gmail.com";
        String serialNumber = "123215";
        String type = "typeAutomaticTestPut";

        JsonObject value = createItemRequestBody( assignee, inventoryNumber, location,
                model, owner, serialNumber, type );

        given().headers(

                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON).

                body(value).when().
                put("/api/items/12").
                then().statusCode(403).
                log().all();
    }
    @Test(description = "TC69" , testName = "Try to delete an item without an authorization token")
    public static void deleteTypeWithoutAuthentication() {

        given().headers(
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().delete("/api/items/12").
                then().
                statusCode(403).
                log().all();

    }
}
