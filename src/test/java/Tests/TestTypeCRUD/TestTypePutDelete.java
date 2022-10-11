package Tests.TestTypeCRUD;

import Framework.BaseClass;
import com.github.cliftonlabs.json_simple.JsonObject;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static Framework.BaseClass.accessToken;
import static Framework.CRUDTypes.createTypeRequestBody;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class TestTypePutDelete {


        @BeforeTest
        public static  void SetAuthToken (){
            baseURI = BaseClass.baseURI;
            accessToken = BaseClass.setAuthToken();
        }

        @Test(description = "TC46", testName = "Edit an existing type")
        public static void putTypeTestValid( ){
            String name = "typeAutomaticTestPut";


            JsonObject value =  createTypeRequestBody( name);

            given().headers(
                            "Authorization",
                            "Bearer " + accessToken,
                            "Content-Type",
                            ContentType.JSON,
                            "Accept",
                            ContentType.JSON).

                    body(value).when().
                    put("/api/types/12").
                    then().statusCode(200).
                    body("name",equalTo("typeAutomaticTestPut")).
                    log().all();
        }
        @Test(description = "TC47", testName = "Try to edit an not-existing type")
        public static void putTestTypeNotExist( ){
            String name = "typeAutomaticTestPut";


            JsonObject value =  createTypeRequestBody( name);

            given().headers(
                            "Authorization",
                            "Bearer " + accessToken,
                            "Content-Type",
                            ContentType.JSON,
                            "Accept",
                            ContentType.JSON).

                    body(value).when().
                    put("/api/types/1200").
                    then().statusCode(404);



        }
        @Test(description = "TC",  testName = "Try to edit an not-existing type with incorrect format id")
        public static void putTestTypeInvalidFormatInput(){
            String name = "";


            JsonObject value =  createTypeRequestBody( name);

            given().headers(
                            "Authorization",
                            "Bearer " + accessToken,
                            "Content-Type",
                            ContentType.JSON,
                            "Accept",
                            ContentType.JSON).

                    body(value).when().
                    put("/api/types/12").
                    then().statusCode(400).
                    log().all();
        }

        @Test(description = "TC48" , testName = " Delete an existing type")
        public static void deleteSingleTypeValid() {

            given().headers(
                            "Authorization",
                            "Bearer " + accessToken,
                            "Accept",
                            ContentType.JSON)
                    .accept(ContentType.JSON).
                    when().delete("/api/types/20").
                    then().
                    statusCode(204).
                    log().all();

        }

        @Test(description = "TC49", testName = "Try to delete a type with an id, who does not exist in the db" )
        public static void deleteSingleTypeNotFound() {

            given().headers(
                            "Authorization",
                            "Bearer " + accessToken,
                            "Accept",
                            ContentType.JSON)
                    .accept(ContentType.JSON).
                    when().delete("/api/types/200").
                    then().
                    statusCode(404).body(containsString("not found")).
                    log().all();

        }

        @Test(description = "TC50", testName = "Try to delete a type with incorrect format id")
        public static void deleteSingleTypeBadRequest() {

            given().headers(
                            "Authorization",
                            "Bearer " + accessToken,
                            "Accept",
                            ContentType.JSON)
                    .accept(ContentType.JSON).
                    when().delete("/api/types/asvgwasegews").
                    then().
                    statusCode(400).
                    log().all();

        }

        @Test(description = "TC51", testName = "Try to update a type without an authorization token")
        public static void putTypeWithoutAuthentication( ){
            String name = "typeAutomaticTestPut";

            JsonObject value =  createTypeRequestBody( name);
            given().headers(

                            "Content-Type",
                            ContentType.JSON,
                            "Accept",
                            ContentType.JSON).

                    body(value).when().
                    put("/api/types/12").
                    then().statusCode(403).
                    log().all();
        }
        @Test(description = "TC52" , testName = "Try to delete a type without an authorization token")
        public static void deleteTypeWithoutAuthentication() {

            given().headers(
                            "Accept",
                            ContentType.JSON)
                    .accept(ContentType.JSON).
                    when().delete("/api/types/12").
                    then().
                    statusCode(403).
                    log().all();

        }

    }


