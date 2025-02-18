package com.marcapo.template.documents;

import com.marcapo.template.service.ValidPassword;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    @NotNull
    private String username;

    @NotNull
    private String password;
    private String birthDate;
    @Indexed(unique = true)
    @NotNull
    private String email;

    public User() {
    }
}
