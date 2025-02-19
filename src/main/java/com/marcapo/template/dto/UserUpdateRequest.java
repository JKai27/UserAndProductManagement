package com.marcapo.template.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.marcapo.template.service.ValidPassword;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String birthDate;

    private String email;
    private String currentPassword;

    @ValidPassword
    @JsonProperty("password")
    private String newPassword;


}

