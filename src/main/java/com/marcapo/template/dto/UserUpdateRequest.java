package com.marcapo.template.dto;

import com.marcapo.template.service.ValidPassword;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String birthDate;
    private String email;
    private String currentPassword;
    @ValidPassword
    private String newPassword;
}

