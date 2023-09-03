import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CourierCreation extends Client {

    static final String COURIER_PATH = "/api/v1/courier/";

    @Step("Создание курьера")
    public Response create(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }

    @Step("Логин курьера")
    public static Response login(CourierCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post (COURIER_PATH + "login");
    }

    @Step("Удаление курьера")
    public static void delete(String courierId) {
        given()
                .spec(getBaseSpec())
                .body(courierId)
                .when()
                .delete(COURIER_PATH + courierId);
    }
}