package client.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TokenResponse {

    private String access_token;
    private String token_type;
    private int expires_in;
    private String grant_type;
}
