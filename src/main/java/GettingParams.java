import net.datafaker.Faker;

public class GettingParams {
    static Faker faker = new Faker();

    public static Courier getRandomCourier() {
        String login = faker.name().username();
        String password = String.valueOf(faker.password());
        String firstName = faker.name().firstName();
        return new Courier(login, password, firstName);
    }

    public static Order getRandomWithoutColor(String[] color) {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String address = faker.address().fullAddress();
        String metroStation = faker.number().toString();
        String phone = faker.phoneNumber().toString();
        int rentTime = faker.number().numberBetween(1,7);
        String deliveryDate = "2023-09-01";
        String comment = "Блаблабла";
        return new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }
}