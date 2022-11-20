import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class LoginTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    public void createNewCourier() {
        File json = new File("src/test/resources/newCourier.json");
        Response response = given().header("Content-type", "application/json").and().body(json).when().post("/api/v1/courier");
    }


        @Test
        public void loginCourier() {
            createNewCourier();
            File json = new File("src/test/resources/loginCourier.json");
            Response response1 =
                    given()
                            .header("Content-type", "application/json")
                            .and()
                            .body(json)
                            .when()
                            .post("/api/v1/courier/login");
            response1.then().assertThat().statusCode(200).and().body("id", notNullValue());

            deleteCourier(json);
        }

    @Test
    public void loginCourierWithoutLogin() {
        createNewCourier();
        File json = new File("src/test/resources/withoutLoginCourier.json");
        Response response1 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        response1.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));

        deleteCourier(json);
    }

    @Test
    public void loginCourierWithoutPassword() {
        createNewCourier();
        File json = new File("src/test/resources/onlyLoginCourier.json");
        Response response1 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        response1.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));

        deleteCourier(json);
    }

    @Test
    public void loginCourierNotCorrect() {
        createNewCourier();
        File json = new File("src/test/resources/loginNotCorrectCourier.json");
        Response response1 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        response1.then().assertThat().statusCode(404).and().body("message", equalTo("Учетная запись не найдена"));

        deleteCourier(json);
    }


    @Test
    public void loginCourierNotCorrectPassword() {
        createNewCourier();
        File json = new File("src/test/resources/loginNotCorrectPassword.json");
        Response response1 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        response1.then().assertThat().statusCode(404).and().body("message", equalTo("Учетная запись не найдена"));

        deleteCourier(json);
    }
    public void deleteCourier(File courierJson) {
        Response response0 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courierJson)
                        .when()
                        .post("/api/v1/courier/login");

        JsonPath jsonBody = response0.body().jsonPath();

        if(response0.statusCode() == 200) {
            String courierId = jsonBody.getString("id");
            //Response deleteResponse =
            given()
                    .delete("/api/v1/courier/" + courierId);
        }
    }
    }

