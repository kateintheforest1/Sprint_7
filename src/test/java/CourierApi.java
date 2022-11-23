import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.File;

import static io.restassured.RestAssured.given;

public class CourierApi {
    public CourierApi() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    public Response create(File json) {
        Response response = given().header("Content-type", "application/json").and().body(json).when().post("/api/v1/courier");

        return response;
    }

    public void delete(File json) {
        Response response0 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");

        JsonPath jsonBody = response0.body().jsonPath();

        if(response0.statusCode() == 200) {
            String courierId = jsonBody.getString("id");

            given().delete("/api/v1/courier/" + courierId);
        }
    }

    public Response login(File json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login");
    }
}
