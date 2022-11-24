import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import com.google.gson.Gson;
import io.qameta.allure.junit4.DisplayName;
import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private final String orderDataJson;

    public CreateOrderTest(String json) {
        this.orderDataJson = json;
    }

    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        Gson gson = new Gson();

        String[] blackColor = {"BLACK"};
        String[] greyColor = {"GREY"};
        String[] bothColors = {"BLACK", "GREY"};
        String[] noColors = {};

        return new Object[][]{
            {
                gson.toJson(new Order(blackColor))
            },
            {
                gson.toJson(new Order(greyColor))
            },
            {
                gson.toJson(new Order(bothColors))
            },
            {
                gson.toJson(new Order(noColors))
            }
        };
    }

    @Test
    @DisplayName("Check order creating")
    @Step("Send POST request to /api/v1/orders with black, grey, both and no colors and check response status in each case")
    public void createOrder() {
        OrderApi api = new OrderApi();

        api.create(this.orderDataJson).then().statusCode(SC_CREATED).and().extract().path("track");
    }
}
