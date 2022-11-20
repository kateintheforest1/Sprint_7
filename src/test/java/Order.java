import java.lang.reflect.Array;

public class Order {
    private String firstName = "Naruto";
    private String lastName = "Uchiha";
    private String address = "Konoha, 142 apt.";
    private Integer metroStation = 4;
    private String phone = "+7 800 355 35 35";
    private Integer rentTime = 5;
    private String deliveryDate = "2022-12-06";
    private String comment =  "Saske, come back to Konoha";
    private String[] color;

    public Order(String[] color) {
        this.color = color;
    }

}
