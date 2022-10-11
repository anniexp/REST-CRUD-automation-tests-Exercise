package Framework;

import com.github.cliftonlabs.json_simple.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static Framework.Login.createLoginRequestBody;

public class BaseClass {
    public static String accessToken;
    public static String baseURI = "http://xprmnt.xyz:8095";

    public static String setAuthToken() {
        String email = "user1@gmail.com";
        String password = "TestPassword1*";
        JsonObject value = createLoginRequestBody(email, password);

        accessToken = RestAssured.given().
                header("Content-Type", "application/json").
                contentType(ContentType.JSON).accept(ContentType.JSON).
                body(value).when().
                post("/api/login").then().statusCode(200).log().all().extract().path("jwt");

        accessToken = modifyBearerToken(accessToken);

        return accessToken;
    }

    public static String substringString(String stringToBeSplit) {
        String substr = stringToBeSplit.substring(6);
        System.out.println(substr);

        return substr;
    }

    public static String modifyBearerToken(String accessToken) {
        return substringString(accessToken);
    }
}
