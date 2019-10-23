import client.PayUClient;
import client.model.*;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Buyer buyer = Buyer.builder()
                .email("buyer@example.com")
                .firstName("buyer")
                .lastName("buyer")
                .phone("123456789")
                .language("pl")
                .build();

        Product product1 = Product.builder()
                .name("Wireless mouse")
                .unitPrice("15000")
                .quantity("1")
                .build();
        Product product2 = Product.builder()
                .name("HDMI cable")
                .unitPrice("6000")
                .quantity("1")
                .build();


        PayUClient payUClient = new PayUClient();
        try {
            TokenResponse tokenResponse = payUClient.getToken("", "");
            OrderCreateResponse orderCreateResponse = payUClient.createNewOrder(buyer, Arrays.asList(product1, product2), "127.0.0.1", tokenResponse.getAccess_token());
            System.out.println(orderCreateResponse);
            OrderRetrieveResponse orderRetrieveResponse = payUClient.getOrder(orderCreateResponse.getOrderId(), tokenResponse.getAccess_token());
            System.out.println(orderRetrieveResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
