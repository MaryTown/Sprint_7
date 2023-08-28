import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CourierLoginTest {
    private Courier courier;
    private CourierCreation courierCreation;
    private int courierId;
    CourierCredentials  courierCredentials;


    @Before
    public void setUp() {
        courier = GettingParams.getRandomCourier();
        courierCreation = new CourierCreation();
        courierCreation.create(courier);
    }

    @After
    public void cleanUp() {
        courierCreation.delete(String.valueOf(courierId));
    }

    @Test
    @DisplayName("Успешная авторизация курьера")
    public void positiveCourierLoginTest() {
        Response response = CourierCreation.login(CourierCredentials.from(courier));
        assertEquals(200,response.statusCode());
        courierId = CourierCreation.login(CourierCredentials.from(courier)).path("id");
    }

    @Test
    @DisplayName("Авторизация курьера без логина")
    public void courierLoginEmptyLoginTest() {
        Response response = CourierCreation.login(new CourierCredentials(null, courier.getPassword()));
        courierId = CourierCreation.login(CourierCredentials.from(courier)).path("id");
        assertEquals(400,response.statusCode());
        assertEquals("Недостаточно данных для входа", response.path("message"));
    }

    @Test
    @DisplayName("Авторизация курьера без пароля")
    public void courierLoginEmptyPasswordTest() {
        Response response = CourierCreation.login(new CourierCredentials(courier.getLogin(),null));
        courierId = CourierCreation.login(CourierCredentials.from(courier)).path("id");
        assertEquals(400,response.statusCode());
        assertEquals("Недостаточно данных для входа", response.path("message"));
    }

    @Test
    @DisplayName("Авторизация по несуществующему логину")
    public void courierLoginNotExistsLoginTest() {
        CourierCredentials noExistsCourierCredentials = new CourierCredentials("blablabla", "12345");
        courierCredentials = CourierCredentials.from(courier);
        Response response = CourierCreation.login(noExistsCourierCredentials);
        assertEquals(404, response.statusCode());
        assertEquals("Учетная запись не найдена", response.path("message"));
    }

    @Test
    @DisplayName("Авторизация по неправильному паролю")
    public void courierLoginWrongPasswordTest() {
        Response response = CourierCreation.login(new CourierCredentials(courier.getLogin(),"asdfjhgasfd"));
        courierId = CourierCreation.login(CourierCredentials.from(courier)).path("id");
        assertEquals(404, response.statusCode());
        assertEquals("Учетная запись не найдена", response.path("message"));
    }

}