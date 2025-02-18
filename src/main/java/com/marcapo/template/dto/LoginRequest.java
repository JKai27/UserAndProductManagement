package com.marcapo.template.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class LoginRequest {
    private String username;
    private String email;
    @NonNull
    private String password;

}
