import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import org.apache.http.HttpStatus;

public class CourierCreateTest {
    private Courier courier;
    private CourierCreation courierCreation;
    private Integer courierId;

    @Before
    public void setUp() {
        courierCreation = new CourierCreation();
    }

    @After
    public void cleanUp() {
        if (courierId != null) {
            CourierCreation.delete(String.valueOf(courierId));
        }
    }

    @Test
    @DisplayName("Успешное создание курьера")
    public void positiveCreateNewCourierTest() {
        courier = GettingParams.getRandomCourier();
        Response response = courierCreation.create(courier);
        assertEquals("Неверный код ответа", 201, response.statusCode());
        response.then().assertThat().body("ok", equalTo(true));
        Response responseLogin = courierCreation.login(CourierCredentials.from(courier));
        assertEquals("Логин не удался. Курьер ранее не создан", 200, responseLogin.statusCode());
        courierId = responseLogin.path("id");
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void createTheSameCouriersTest() {
        Courier courierDublicate = new Courier(GettingParams.getRandomCourier().getFirstName(),
                GettingParams.getRandomCourier().getLogin(),
                GettingParams.getRandomCourier().getPassword());
        courierCreation.create(courierDublicate);
        Response response = courierCreation.create(courierDublicate);
        assertEquals(HttpStatus.SC_CONFLICT,response.statusCode());
        response.then().assertThat().statusCode(409);
        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
        Response responseLogin = courierCreation.login(CourierCredentials.from(courierDublicate));
        courierId = responseLogin.path("id");
    }

    @Test
    @DisplayName("Нельзя создать курьера с login=null")
    public void createCourierWithNullLoginTest() {
        Courier courierTest = new Courier(null,
                GettingParams.getRandomCourier().getPassword(),
                GettingParams.getRandomCourier().getFirstName());
        Response response = courierCreation.create(courierTest);
        assertEquals("Неверный код ответа при попытке создания курьера без логина", 400, response.statusCode());
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Нельзя создать курьера с password=null")
    public void createCourierWithNullPasswordTest() {
        Courier courierTest = new Courier(GettingParams.getRandomCourier().getLogin(),
                null,
                GettingParams.getRandomCourier().getFirstName());
        Response response = courierCreation.create(courierTest);
        assertEquals("Неверный код ответа при попытке создания курьера без пароля", 400, response.statusCode());
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Нельзя создать курьера с firstName=null")
    public void createCourierWithNullFirstNameTest() {
        Courier courierTest = new Courier(GettingParams.getRandomCourier().getLogin(),
                GettingParams.getRandomCourier().getPassword(),
                null);
        Response response = courierCreation.create(courierTest);
        assertEquals("Неверный код ответа при попытке создания курьера без имени", 400, response.statusCode());
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
