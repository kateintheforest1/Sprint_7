import com.google.gson.Gson;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.*;

public class CreateCourierTest {
    CourierApi courierApi;
    private String courierName = "ldld";
    private String courierSecondName = "mkmk";
    private String courierPassword = "1234";
    private String courierFirstName = "saske";

    public CreateCourierTest() {
        this.courierApi = new CourierApi();
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        Courier courier = new Courier();
        courier.setLogin(courierName);
        courier.setPassword(courierPassword);
        courier.setFirstName(courierFirstName);
        courierApi.delete(courier);
    }

    @After
    public void cleanUp() {
        Courier courier = new Courier();
        courier.setLogin(courierName);
        courier.setPassword(courierPassword);
        courier.setFirstName(courierFirstName);

        courierApi.delete(courier);

        Courier courierWithoutName = new Courier();
        courierWithoutName.setLogin(courierSecondName);
        courierWithoutName.setPassword(courierPassword);

        courierApi.delete(courierWithoutName);
    }

    @Test
    @DisplayName("Check courier creating")
    @Step("Send POST request to /api/v1/courier and check response status")
    public void createNewCourierTest() {
        Gson gson = new Gson();
        Courier courier = new Courier();
        courier.setLogin(courierName);
        courier.setPassword(courierPassword);
        courier.setFirstName(courierFirstName);

        courierApi.create(gson.toJson(courier))
                .then()
                .statusCode(SC_CREATED);
    }


    @Test
    @DisplayName("Check twice creating the same courier")
    @Step("Send the same POST request to /api/v1/courier twice and check response status")

    public void createTheSameCourierTest() {
        Gson gson = new Gson();
        Courier courier = new Courier();
        courier.setLogin(courierName);
        courier.setPassword(courierPassword);
        courier.setFirstName(courierFirstName);

        courierApi.create(gson.toJson(courier)).then().statusCode(SC_CREATED);
        courierApi.create(gson.toJson(courier)).then().assertThat().statusCode(SC_CONFLICT).and().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Check creating courier without login")
    @Step("Send POST request to /api/v1/courier without login and check response status")
    public void createCourierWithoutLoginTest() {
        Gson gson = new Gson();
        Courier courier = new Courier();
        courier.setPassword(courierPassword);
        courier.setFirstName(courierFirstName);

        courierApi.create(gson.toJson(courier))
                .then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check creating courier without password")
    @Step("Send POST request to /api/v1/courier without password and check response status")
    public void createCourierWithoutPasswordTest() {
        Gson gson = new Gson();
        Courier courier = new Courier();
        courier.setLogin(courierName);
        courier.setFirstName(courierPassword);

        courierApi.create(gson.toJson(courier))
                .then()
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }


    @Test
    @DisplayName("Check creating courier without name")
    @Step("Send POST request to /api/v1/courier without name and check response status")
    public void createCourierWithoutNameTest() {
        Gson gson = new Gson();
        Courier courier = new Courier();
        courier.setLogin(courierSecondName);
        courier.setPassword(courierPassword);

        courierApi.create(gson.toJson(courier)).then().statusCode(SC_CREATED);
    }
}
