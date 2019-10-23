package client.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderCreateResponse {

    private Status status;

    private String redirectUri;

    private String orderId;
}
