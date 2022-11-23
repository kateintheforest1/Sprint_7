import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OrderApi {
    public OrderApi() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    public Response create(String json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/orders");
    }
}
