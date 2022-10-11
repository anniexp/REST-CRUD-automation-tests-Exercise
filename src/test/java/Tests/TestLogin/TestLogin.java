package Tests.TestLogin;

import Framework.BaseClass;
import com.github.cliftonlabs.json_simple.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static Framework.BaseClass.accessToken;
import static Framework.Login.createLoginRequestBody;
import static io.restassured.RestAssured.baseURI;
import static org.hamcrest.Matchers.containsString;


public class TestLogin {
    @BeforeTest
    public static  void SetAuthToken (){
        baseURI = BaseClass.baseURI;
        accessToken = BaseClass.setAuthToken();
    }

    @Test(description = "test login request with valid credentials", testName = "TC1")
    public static void testLoginWithValidCredentials(){

        JsonObject value = new JsonObject();
        value.put("email","user1@gmail.com");
        value.put("password","TestPassword1*");

         RestAssured.given().
                header("Content-Type","application/json").
                contentType(ContentType.JSON).accept(ContentType.JSON).
                body(value).when().
                post("/api/login").then().statusCode(200).log().all().extract().path("jwt");

    }

    @Test(description = "test login requests with invalid credentials", testName = "TC9",
            dataProvider = "LoginInvalidDataProvider")
    public static void testLoginWithInValidCredentials(String inputEmail, String inputPassword){

       JsonObject value  = createLoginRequestBody( inputEmail,  inputPassword );

         RestAssured.given().
                header("Content-Type","application/json").
                contentType(ContentType.JSON).accept(ContentType.JSON).
                body(value).when().
                post("/api/login").then().statusCode(400).body(containsString("Invalid credentials")).log().all();

    }

    @DataProvider(name = "LoginInvalidDataProvider")
    public Object[][] loginInvalidDataProvider() {
        return new Object[][]
                {
                        {"user1@gmail.com", ""},
                        {"", "TestPassword1*"},
                        {"", "" }

                };

    }
}
