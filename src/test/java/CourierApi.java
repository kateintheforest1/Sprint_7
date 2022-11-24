import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static org.apache.http.HttpStatus.*;

import static io.restassured.RestAssured.given;

public class CourierApi {
    public CourierApi() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    public Response createCourier(Courier courier) {
        Gson gson = new Gson();
        Response response = given().header("Content-type", "application/json").and().body(gson.toJson(courier)).when().post("/api/v1/courier");

        return response;
    }

    public void delete(Courier courier) {
        Gson gson = new Gson();

        Response response0 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(gson.toJson(courier))
                        .when()
                        .post("/api/v1/courier/login");

        JsonPath jsonBody = response0.body().jsonPath();

        if(response0.statusCode() == SC_OK) {
            String courierId = jsonBody.getString("id");

            given().delete("/api/v1/courier/" + courierId);
        }
    }

    public Response login(Courier courier) {
        Gson gson = new Gson();

        return given()
                .header("Content-type", "application/json")
                .and()
                .body(gson.toJson(courier))
                .when()
                .post("/api/v1/courier/login");
    }
}
