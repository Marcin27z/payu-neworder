package client.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderRetrieveResponse {

    private List<Order> orders;

    private Status status;
}
