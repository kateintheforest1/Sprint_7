import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import com.google.gson.Gson;

import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }
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
    public void createOrder() {
        Response createOrderResponse =
                given()
                    .header("Content-type", "application/json")
                    .and()
                    .body(this.orderDataJson)
                    .when()
                    .post("/api/v1/orders");

        createOrderResponse.then().statusCode(201).and().extract().path("track");
    }
}
