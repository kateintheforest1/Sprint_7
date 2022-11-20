import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateCourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void createNewCourier() {
        File json = new File("src/test/resources/newCourier.json");
        Response response = given().header("Content-type", "application/json").and().body(json).when().post("/api/v1/courier");
        response.then().statusCode(201).and().extract().path("ok");

        deleteCourier(json);
    }

    @Test
    public void createTheSameCourier() {
        File json = new File("src/test/resources/newCourier.json");
        Response response1 = given().header("Content-type", "application/json").and().body(json).when().post("/api/v1/courier");
        response1.then().statusCode(201).and().extract().path("ok");

        Response response2 = given().header("Content-type", "application/json")
                .and().body(json).when().post("/api/v1/courier");
        response2.then().assertThat().statusCode(409).and().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        deleteCourier(json);
    }

    @Test
    public void createCourierWithoutLogin() {
        File json = new File("src/test/resources/withoutLoginCourier.json");
        Response response3 = given().header("Content-type", "application/json")
                //.auth().oauth2("подставь_сюда_свой_токен")
                .and().body(json).when().post("/api/v1/courier");

        response3.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Test
    public void createCourierWithoutPassword() {
        File json = new File("src/test/resources/withoutPasswordCourier.json");
        Response response4 = given().header("Content-type", "application/json")
                //.auth().oauth2("подставь_сюда_свой_токен")
                .and().body(json).when().post("/api/v1/courier");
        response4.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }


    @Test
   public void createCourierWithoutName() {
        File json = new File("src/test/resources/withoutNameCourier.json");
        Response response5 = given().header("Content-type", "application/json")
                //.auth().oauth2("подставь_сюда_свой_токен")
                .and().body(json).when().post("/api/v1/courier");

        System.out.println(response5.body().asString());
        response5.then().statusCode(201).and().extract().path("ok");
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
