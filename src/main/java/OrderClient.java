import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderClient extends Client {

    static final String ORDER_PATH ="/api/v1/orders";

    @Step("Создание заказа")
    public ValidatableResponse create(Order order) {
        return (ValidatableResponse) given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Список заказов")
    public ValidatableResponse getOrderList() {
        return (ValidatableResponse) given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }
}