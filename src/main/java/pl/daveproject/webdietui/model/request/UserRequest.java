package pl.daveproject.webdietui.model.request;

import lombok.Data;

@Data
public class UserRequest implements WebdietRequest {
    private String username;
    private String password;
    private String lastName;
    private String firstName;
}
