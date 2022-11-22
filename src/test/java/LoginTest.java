import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
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

    @After
    public void cleanUp() {
        deleteCourier(new File("src/test/resources/newCourier.json"));
    }


        @Test
        @DisplayName("Check login by courier")
        public void loginCourierTest() {
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

            //deleteCourier(json);
        }

    @Test
    @DisplayName("Check login by courier without entering login")
    public void loginCourierWithoutLoginTest() {
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

        //deleteCourier(json);
    }

    @Test
    @DisplayName("Check login by courier without entering password")
    public void loginCourierWithoutPasswordTest() {
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

        //deleteCourier(json);
    }

    @Test
    @DisplayName("Check login by courier with incorrect login")
    public void loginCourierNotCorrectTest() {
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

        //deleteCourier(json);
    }


    @Test
    @DisplayName("Check login by courier with incorrect password")
    public void loginCourierNotCorrectPasswordTest() {
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

        //deleteCourier(json);
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

