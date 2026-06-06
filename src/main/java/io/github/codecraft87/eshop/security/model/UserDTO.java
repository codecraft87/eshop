package io.github.codecraft87.eshop.security.model;

import java.util.List;

import lombok.Data;

@Data
public class UserDTO {

    private String username;
    private String password;
    private List<String> roles;
}
