import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class LoginTest {
    private CourierApi courierApi;
    public LoginTest() {
        CourierApi api = new CourierApi();
        this.courierApi = api;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    public void createNewCourier() {
        Gson gson = new Gson();
        Courier courier = new Courier();
        courier.setLogin("ldld");
        courier.setPassword("1234");
        courier.setFirstName("saske");
        courierApi.create(gson.toJson(courier));
    }

    @After
    public void cleanUp() {
        Courier courier = new Courier();
        courier.setLogin("ldld");
        courier.setFirstName("saske");
        courier.setPassword("1234");
        courierApi.delete(courier);
    }

    @Test
    @DisplayName("Check login by courier")
    @Step("Send POST request to /api/v1/courier/login and check response status")
    public void loginCourierTest() {
        createNewCourier();

        Courier courier = new Courier();
        courier.setLogin("ldld");
        courier.setPassword("1234");

        courierApi
                .login(courier)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Check login by courier without entering login")
    @Step("Send POST request to /api/v1/courier/login without entering login and check response status")
    public void loginCourierWithoutLoginTest() {
        createNewCourier();
        Courier courier = new Courier();

        courier.setPassword("1234");

        courierApi.login(courier).then().assertThat().statusCode(SC_BAD_REQUEST).and().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check login by courier without entering password")
    @Step("Send POST request to /api/v1/courier/login without entering password and check response status")
    public void loginCourierWithoutPasswordTest() {
        createNewCourier();
        Courier courier = new Courier();
        courier.setLogin("ldld");
        courier.setPassword("");
        courierApi.login(courier).then().assertThat().statusCode(SC_BAD_REQUEST).and().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check login by courier with incorrect login")
    @Step("Send POST request to /api/v1/courier/login with entering incorrect login and check response status")
    public void loginCourierNotCorrectTest() {
        createNewCourier();

        Courier courier = new Courier();
        courier.setLogin("kjlkj");
        courier.setPassword("1234");
        courierApi.login(courier).then().assertThat().statusCode(SC_NOT_FOUND).and().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check login by courier with incorrect password")
    @Step("Send POST request to /api/v1/courier/login with entering incorrect password and check response status")
    public void loginCourierNotCorrectPasswordTest() {
        createNewCourier();
        Courier courier = new Courier();
        courier.setLogin("ldld");
        courier.setPassword("ljljh");
        courierApi.login(courier).then().assertThat().statusCode(SC_NOT_FOUND).and().body("message", equalTo("Учетная запись не найдена"));
    }
}

