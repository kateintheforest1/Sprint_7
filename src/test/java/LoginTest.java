import com.google.gson.Gson;
import io.qameta.allure.Step;
import org.junit.*;
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
    public void createNewCourier() {
        Gson gson = new Gson();
        Courier courier = new Courier();
        courier.setLogin("ldld");
        courier.setPassword("1234");
        courier.setFirstName("saske");
        courierApi.createCourier(gson.toJson(courier));
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
        Courier courier = new Courier();
        courier.setPassword("1234");

        courierApi.login(courier).then().assertThat().statusCode(SC_BAD_REQUEST).and().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check login by courier without entering password")
    @Step("Send POST request to /api/v1/courier/login without entering password and check response status")
    public void loginCourierWithoutPasswordTest() {
        Courier courier = new Courier();
        courier.setLogin("ldld");
        courier.setPassword("");

        courierApi.login(courier).then().assertThat().statusCode(SC_BAD_REQUEST).and().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check login by courier with incorrect login")
    @Step("Send POST request to /api/v1/courier/login with entering incorrect login and check response status")
    public void loginCourierNotCorrectTest() {
        Courier courier = new Courier();
        courier.setLogin("kjlkj");
        courier.setPassword("1234");
        courierApi.login(courier).then().assertThat().statusCode(SC_NOT_FOUND).and().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check login by courier with incorrect password")
    @Step("Send POST request to /api/v1/courier/login with entering incorrect password and check response status")
    public void loginCourierNotCorrectPasswordTest() {
        Courier courier = new Courier();
        courier.setLogin("ldld");
        courier.setPassword("ljljh");
        courierApi.login(courier).then().assertThat().statusCode(SC_NOT_FOUND).and().body("message", equalTo("Учетная запись не найдена"));
    }
}

