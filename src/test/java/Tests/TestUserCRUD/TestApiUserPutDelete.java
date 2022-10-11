package Tests.TestUserCRUD;

import Framework.BaseClass;
import com.github.cliftonlabs.json_simple.JsonObject;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static Framework.BaseClass.accessToken;
import static Framework.CRUDUsers.createUserRequestBody;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class TestApiUserPutDelete {
    @BeforeTest
    public static  void SetAuthToken (){
        baseURI = BaseClass.baseURI;
        accessToken = BaseClass.setAuthToken();
    }

    @Test(description = "TC10", testName = "Update an existing user with valid request")
    public static void putTestValid( ){
        String email = "dddfsgsj@fsf";
        String fullName =  "geg geg";
        String password =  "TestPass3*";

        JsonObject value =  createUserRequestBody( email,  fullName, password );

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON).

                body(value).when().
                put("/api/users/39").
                then().statusCode(200).
                body("fullName",equalTo("geg geg")).
                log().all();
    }
    @Test(description = "TC11",  testName = "Try to update an not existing user")
    public static void putTestUserNotExist( ){
        String email = "dddfsgsj@fsf";
        String fullName =  "geg geg";
        String password =  "TestPass3*";

        JsonObject value =  createUserRequestBody( email,  fullName, password );

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON).

                body(value).when().
                put("/api/users/200").
                then().statusCode(404);



    }
    @Test(description = "TC12",  testName = "Try to update an existing user with invalid request")
    public static void putTestInvalidFormatInput(){
        String email = "dddfsgsj@fsf";
        String fullName =  "geg geg";
        String password =  "";

        JsonObject value =  createUserRequestBody( email,  fullName, password );

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON).

                body(value).when().
                put("/api/users/39").
                then().statusCode(400).
                log().all();
    }

    @Test(description = "TC13",  testName = "Delete an existing user")
    public static void deleteSingleUserValid() {

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().delete("/api/users/20").
                then().
                statusCode(204).
                log().all();

    }

    @Test(description = "TC14",  testName = "Try to delete an not existing user ")
    public static void deleteSingleUserNotFound() {

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().delete("/api/users/200").
                then().
                statusCode(404).body(containsString("not found")).
                log().all();

    }

    @Test(description = "TC15",  testName = "Try to delete an user with invalid id format")
    public static void deleteSingleUserBadRequest() {

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().delete("/api/users/asvgwasegews").
                then().
                statusCode(400).
                log().all();

    }

    @Test(description = "TC17", testName = "Try to update an user without an authorization token")
    public static void putUserWithoutAuthentication( ){
        String email = "dddfsgsj@fsf";
        String fullName =  "geg geg";
        String password =  "TestPass3*";

        JsonObject value =  createUserRequestBody( email,  fullName, password );

        given().headers(

                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON).

                body(value).when().
                put("/api/users/20").
                then().statusCode(403).
                log().all();
    }
    @Test(description = "TC18" , testName = "Try to delete an user without an authorization token")
    public static void deleteUserWithoutAuthentication() {

        given().headers(
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().delete("/api/users/20").
                then().
                statusCode(403).
                log().all();

    }

}
