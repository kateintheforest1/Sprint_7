import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import java.io.File;
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
        File json = new File("src/test/resources/newCourier.json");
        courierApi.create(json);
    }

    @After
    public void cleanUp() {
        courierApi.delete(new File("src/test/resources/newCourier.json"));
    }


    @Test
    @DisplayName("Check login by courier")
    public void loginCourierTest() {
        createNewCourier();

        File json = new File("src/test/resources/loginCourier.json");
        courierApi
                .login(json)
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

        File json = new File("src/test/resources/withoutLoginCourier.json");
        courierApi.login(json).then().assertThat().statusCode(SC_BAD_REQUEST).and().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check login by courier without entering password")
    public void loginCourierWithoutPasswordTest() {
        createNewCourier();
        File json = new File("src/test/resources/onlyLoginCourier.json");
        courierApi.login(json).then().assertThat().statusCode(SC_BAD_REQUEST).and().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check login by courier with incorrect login")
    public void loginCourierNotCorrectTest() {
        createNewCourier();

        File json = new File("src/test/resources/loginNotCorrectCourier.json");
        courierApi.login(json).then().assertThat().statusCode(SC_NOT_FOUND).and().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check login by courier with incorrect password")
    public void loginCourierNotCorrectPasswordTest() {
        createNewCourier();
        File json = new File("src/test/resources/loginNotCorrectPassword.json");
        courierApi.login(json).then().assertThat().statusCode(SC_NOT_FOUND).and().body("message", equalTo("Учетная запись не найдена"));
    }
}

