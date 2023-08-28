import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private Order order;
    private OrderClient orderClient;
    private final String[] color;

    public CreateOrderTest(String color) {
        this.color = new String[]{color};
    }

    @Parameterized.Parameters
    public static Object[][] setColor() {
        return new Object[][]{
                {"BLACK"},
                {"GREY"},
                {"BLACK , GREY"},
                {""}
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка вариативности заполнения поля Цвет самоката")
    public void createNewOrderTest() {
        order = GettingParams.getRandomWithoutColor(color);
        ValidatableResponse response =  orderClient.create(order);
        assertEquals(201, response.extract().statusCode());
        assertNotEquals(Optional.of(0), response.extract().path("track"));
    }
}