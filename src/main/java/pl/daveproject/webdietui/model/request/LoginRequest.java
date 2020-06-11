package pl.daveproject.webdietui.model.request;

import lombok.Data;

@Data
public class LoginRequest implements WebdietRequest {
    private String username;
    private String password;
}
