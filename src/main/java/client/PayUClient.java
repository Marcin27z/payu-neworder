package client;

import client.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PayUClient {

    private ObjectMapper objectMapper = new ObjectMapper();

    private final String orderApiUrl = "https://secure.snd.payu.com/api/v2_1/orders";

    private final String authorizeApiUrl = "https://secure.snd.payu.com/pl/standard/user/oauth/authorize";

    public OrderCreateResponse createNewOrder(Buyer buyer, List<Product> productList, String customerIp, String oAuthToken) throws IOException {

        OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
                .notifyUrl(Shop.NOTIFY_URL)
                .customerIp(customerIp)
                .merchantPosId(Shop.POS_ID)
                .description(Shop.DESCRIPTION)
                .currencyCode("PLN")
                .totalAmount(sumProductsCost(productList))
                .buyer(buyer)
                .products(productList)
                .build();

        URL url = new URL(orderApiUrl);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json");
        connection.setInstanceFollowRedirects(false);
        connection.setRequestProperty("Authorization", "Bearer " + oAuthToken);
        connection.setRequestProperty("Content-type", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        String newOrderRequestJson = objectMapper.writeValueAsString(orderCreateRequest);
        sendData(connection, newOrderRequestJson);
        return objectMapper.readValue(readResponse(connection), OrderCreateResponse.class);
    }

    public OrderRetrieveResponse getOrder(String orderId, String oAuthToken) throws IOException {
        URL url = new URL(orderApiUrl + "/" + orderId);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", "Bearer " + oAuthToken);
        connection.setDoInput(true);
        return objectMapper.readValue(readResponse(connection), OrderRetrieveResponse.class);
    }


    public TokenResponse getToken(String client_id, String client_secret) throws IOException {

        URL url = new URL(authorizeApiUrl);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setInstanceFollowRedirects(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        String data = "grant_type=client_credentials" + "&client_id=" + client_id + "&client_secret=" + client_secret;
        sendData(connection, data);
        return objectMapper.readValue(readResponse(connection), TokenResponse.class);
    }

    private void sendData(HttpsURLConnection connection, String data) throws IOException {
        OutputStream os = connection.getOutputStream();
        byte[] input = data.getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);
    }

    private String readResponse(HttpsURLConnection connection) throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        return response.toString();
    }

    private String sumProductsCost(List<Product> productList) {
        return Integer.toString(productList.stream()
                .mapToInt(p -> Integer.parseInt(p.getQuantity()) * Integer.parseInt(p.getUnitPrice())).sum());
    }
}

