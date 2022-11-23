import com.google.gson.Gson;
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
    public void loginCourierWithoutLoginTest() {
        createNewCourier();
        Courier courier = new Courier();

        courier.setPassword("1234");

        courierApi.login(courier).then().assertThat().statusCode(SC_BAD_REQUEST).and().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check login by courier without entering password")
    public void loginCourierWithoutPasswordTest() {
        createNewCourier();
        Courier courier = new Courier();
        courier.setLogin("ldld");
        courier.setPassword("");
        courierApi.login(courier).then().assertThat().statusCode(SC_BAD_REQUEST).and().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check login by courier with incorrect login")
    public void loginCourierNotCorrectTest() {
        createNewCourier();

        Courier courier = new Courier();
        courier.setLogin("kjlkj");
        courier.setPassword("1234");
        courierApi.login(courier).then().assertThat().statusCode(SC_NOT_FOUND).and().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check login by courier with incorrect password")
    public void loginCourierNotCorrectPasswordTest() {
        createNewCourier();
        Courier courier = new Courier();
        courier.setLogin("ldld");
        courier.setPassword("ljljh");
        courierApi.login(courier).then().assertThat().statusCode(SC_NOT_FOUND).and().body("message", equalTo("Учетная запись не найдена"));
    }
}

