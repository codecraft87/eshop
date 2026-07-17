package io.github.codecraft87.eshop.security.dto;

import java.util.List;

public record UserRequest(String username, String password, List<String> roles) {}
