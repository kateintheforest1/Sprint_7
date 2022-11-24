import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderApi {
    public OrderApi() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    public Response create(Order order) {
        Gson gson = new Gson();

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(gson.toJson(order))
                .when()
                .post("/api/v1/orders");
    }

    public Response getList() {
        return given()
                .get("/api/v1/orders");
    }
}
