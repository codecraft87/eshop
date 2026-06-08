package io.github.codecraft87.eshop.security.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserRequest {

    private String username;
    private String password;
    private List<String> roles;
}
