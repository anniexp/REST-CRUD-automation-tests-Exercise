package Tests.TestUserCRUD;

import Framework.BaseClass;
import com.github.cliftonlabs.json_simple.JsonObject;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static Framework.BaseClass.accessToken;
import static Framework.CRUDUsers.createUserRequestBody;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class TestApiUserGetPost {
    @BeforeTest
    public static void SetAuthToken() {
        baseURI = BaseClass.baseURI;
        accessToken = BaseClass.setAuthToken();
    }

    @Test(description = "TC2", testName = "Get page 1 with list with first 10 users ")
    public static void getAllUsersValid() {

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON).get("/api/users?page=1&pageSize=10").
                then().
                //assert status code is 200 -OK
                        statusCode(200).body("page", equalTo(1)).log().all().body("page-size", equalTo(10)).
                log().all();
    }

    @Test(description = "TC3", testName = "Get an existing user by id")
    public static void getSingleUserValid() {
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().get("/api/users/2").
                then().
                statusCode(200).body("id", equalTo(2)).
                log().all();

    }

    @Test(description = "TC4", testName = "Try to get an user with an id, which does not exist")
    public static void getSingleUserNotFound() {


        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().get("/api/users/200").
                then().
                statusCode(404).body(containsString("not found")).
                log().all();

    }

    @Test(description = "TC5", testName = "Try to get an user with invalid format id")
    public static void getSingleUserBadRequest() {

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().get("/api/users/asvgwasegews").
                then().
                statusCode(400).
                log().all();

    }

    @Test(description = "TC6" , testName = "Create an user with valid request")
    public static void postUserValid() {
        String email = "dddfsgsj@fsf";
        String fullName = "fsfe svse";
        String password = "TestPass3*";


        JsonObject value = createUserRequestBody(email, fullName, password);
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                body(value).when().
                post("/api/users").then().statusCode(201).log().all();

    }


    @Test(description = "TC7", testName = "Try to create an user with typing in invalid email, fullname and password",
            dataProvider = "userInvalidDataProvider"
    )
    public static void postUserInvalidFormatInput(String emailToCheck,
                                                  String fullNameToCheck,
                                                  String passToCheck) {


        JsonObject value = createUserRequestBody(emailToCheck, fullNameToCheck, passToCheck);
        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                body(value).when().
                post("/api/users").then().statusCode(400).
                body(containsString("Email must")).
                body(containsString("Name should")).body(containsString("Password must")).
                log().all();


    }

    @Test(description = "TC8", testName = "Try to create an user with an email, which already exists in the system")
    public static void postUserWithAlreadyRegisteredEmail() {

        String email = "dddfsgsj@fsf";
        String fullName = "fsfe svse";
        String password = "TestPass3*";

        JsonObject value = createUserRequestBody(email, fullName, password);

        given().headers(
                        "Authorization",
                        "Bearer " + accessToken,
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                body(value).when().
                post("/api/users").then().statusCode(400).body(containsString("Email")).body(containsString("already exists!")).log().all();

    }
    @Test(description = "TC16", testName = "Try to get all users without an authorization token")
    public static void getAllUsersWithoutAuthentication() {

        given().headers(

                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON).get("/api/users?page=1&pageSize=10").
                then().
                        statusCode(403).
                log().all();
    }

    @Test(description = "TC19", testName = "Try to get an user without an authorization token")
    public static void getSingleUserWithoutAuthentication() {

        given().headers(
                        "Content-Type",
                        ContentType.JSON,
                        "Accept",
                        ContentType.JSON)
                .accept(ContentType.JSON).
                when().get("/api/users/1").
                then().
                statusCode(403).
                log().all();

    }

    @DataProvider(name = "userInvalidDataProvider")
    public Object[][] userInvalidDataProvider() {
        return new Object[][]
                {
                        {"", "", ""},
                        {"sregNoDomain", "e", "aaaaaг"},
                        {"w", "e", "t"},
                        {"shgggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggeggg", "sdhgggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg", "shggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg"},

                };

    }
}
