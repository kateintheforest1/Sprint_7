import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import io.qameta.allure.junit4.DisplayName;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.*;

public class CreateCourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        CourierApi api = new CourierApi();
        api.delete(new File("src/test/resources/newCourier.json"));
    }

    @After
    public void cleanUp() {
        CourierApi api = new CourierApi();

        api.delete(new File("src/test/resources/newCourier.json"));
        api.delete(new File("src/test/resources/withoutNameCourier.json"));
    }

    @Test
    @DisplayName("Check courier creating")
    public void createNewCourierTest() {
        CourierApi api = new CourierApi();

        Response response = api.create(new File("src/test/resources/newCourier.json"));

        response.then().statusCode(201).and().statusCode(SC_CREATED);
    }


    @Test
    @DisplayName("Check twice creating the same courier")
    public void createTheSameCourierTest() {
        CourierApi api = new CourierApi();

        File json = new File("src/test/resources/newCourier.json");

        api.create(json).then().statusCode(201).and().statusCode(SC_CREATED);
        api.create(json).then().assertThat().statusCode(409).and().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Check creating courier without login")
    public void createCourierWithoutLoginTest() {
        CourierApi api = new CourierApi();

        api.create(new File("src/test/resources/withoutLoginCourier.json"))
                .then()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check creating courier without password")
    public void createCourierWithoutPasswordTest() {
        File json = new File("src/test/resources/withoutPasswordCourier.json");
        CourierApi api = new CourierApi();

        api.create(json)
                .then()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }


    @Test
    @DisplayName("Check creating courier without name")
    public void createCourierWithoutNameTest() {
        File json = new File("src/test/resources/withoutNameCourier.json");
        CourierApi api = new CourierApi();

        api.create(json).then().statusCode(201).and().statusCode(SC_CREATED);
    }
}
