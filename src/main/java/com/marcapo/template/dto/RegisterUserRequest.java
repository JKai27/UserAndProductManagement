package com.marcapo.template.dto;

import com.marcapo.template.service.ValidPassword;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;


@Data
public class RegisterUserRequest {
    private String firstName;
    private String lastName;
    private String birthDate;
    @Indexed(unique = true)
    @NotNull
    private String username;


    @ValidPassword
    private String password;

    @Indexed(unique = true)
    private String email;
}
