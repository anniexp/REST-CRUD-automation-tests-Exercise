package Tests.TestLocationCRUD;

import Framework.BaseClass;
import com.github.cliftonlabs.json_simple.JsonObject;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static Framework.BaseClass.accessToken;
import static Framework.CRUDLocations.createLocationRequestBody;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class TestPutDeleteLocations {

    @BeforeTest
    public static  void SetAuthToken (){
        baseURI = BaseClass.baseURI;
        accessToken = BaseClass.setAuthToken();
    }

    @Test(description = "TC29", testName = "Edit an existing location")
    public static void putLocationTestValid( ){
        String name = "locationAutomaticTestPutt";
        JsonObject value =  createLocationRequestBody(name);

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON).

                body(value).when().
                put("/api/locations/25").
                then().statusCode(200).
                body("address",equalTo("locationAutomaticTestPutt")).
                log().all();
    }
    @Test(description = "TC30", testName = "Try to edit an not-existing location")
    public static void putLocationNotExist( ){
        String name = "typeAutomaticTestPut";
        JsonObject value =  createLocationRequestBody( name);

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON).

                body(value).when().
                put("/api/locations/1200").
                then().statusCode(404);
    }
    @Test(description = "TC31",  testName = "Try to edit an not-existing location with incorrect format id")
    public static void putTestLocationInvalidFormatInput(){
        String name = "";
        JsonObject value =  createLocationRequestBody( name);

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON).
                body(value).when().
                put("/api/locations/uihuj").
                then().statusCode(400).
                log().all();
    }

    @Test(description = "TC32" , testName = " Delete an existing location")
    public static void deleteSingleLocationValid() {

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().delete("/api/locations/2").
                then().
                statusCode(204).
                log().all();
    }

    @Test(description = "TC33", testName = "Try to delete a location with an id, who does not exist in the db" )
    public static void deleteSingleLocationNotFound() {
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().delete("/api/locations/200").
                then().
                statusCode(404).body(containsString("not found")).
                log().all();
    }

    @Test(description = "TC34", testName = "Try to delete a location with incorrect format id")
    public static void deleteSingleLocationBadRequest() {
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().delete("/api/locations/asvgwasegews").
                then().
                statusCode(400).
                log().all();
    }

    @Test(description = "TC35", testName = "Try to update a location without an authorization token")
    public static void putLocationWithoutAuthentication( ){
        String name = "locationAutomaticTestPut";
        JsonObject value =  createLocationRequestBody( name);
        given().headers(

                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON).

                body(value).when().
                put("/api/locations/12").
                then().statusCode(403).
                log().all();
    }

    @Test(description = "TC36" , testName = "Try to delete a location without an authorization token")
    public static void deleteTypeWithoutAuthentication() {
        given().headers(
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().delete("/api/locations/12").
                then().
                statusCode(403).
                log().all();
    }
}
