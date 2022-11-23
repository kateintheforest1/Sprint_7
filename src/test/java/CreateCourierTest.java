import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import io.qameta.allure.junit4.DisplayName;
import static org.hamcrest.Matchers.*;
import static org.apache.http.HttpStatus.*;

public class CreateCourierTest {
    CourierApi courierApi;

    public CreateCourierTest() {
        this.courierApi = new CourierApi();
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        courierApi.delete(new File("src/test/resources/newCourier.json"));
    }

    @After
    public void cleanUp() {
        courierApi.delete(new File("src/test/resources/newCourier.json"));
        courierApi.delete(new File("src/test/resources/withoutNameCourier.json"));
    }

    @Test
    @DisplayName("Check courier creating")
    public void createNewCourierTest() {
        courierApi.create(new File("src/test/resources/newCourier.json"))
                .then()
                .statusCode(201)
                .and()
                .statusCode(SC_CREATED);
    }


    @Test
    @DisplayName("Check twice creating the same courier")
    public void createTheSameCourierTest() {
        File json = new File("src/test/resources/newCourier.json");

        courierApi.create(json).then().statusCode(201).and().statusCode(SC_CREATED);
        courierApi.create(json).then().assertThat().statusCode(409).and().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Check creating courier without login")
    public void createCourierWithoutLoginTest() {
        courierApi.create(new File("src/test/resources/withoutLoginCourier.json"))
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

        courierApi.create(json)
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

        courierApi.create(json).then().statusCode(201).and().statusCode(SC_CREATED);
    }
}
