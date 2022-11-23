import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check getting order list")
    public void getOrderListTest() {
        OrderApi orderApi = new OrderApi();
        orderApi.getList().then().statusCode(200).and().body("orders", notNullValue());
    }
}