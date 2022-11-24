import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {
   
    @Test
    @DisplayName("Check getting order list")
    @Step("Send GET request to /api/v1/orders and check response status")
    public void getOrderListTest() {
        OrderApi orderApi = new OrderApi();
        orderApi.getList().then().statusCode(200).and().body("orders", notNullValue());
    }
}