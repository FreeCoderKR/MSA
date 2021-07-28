package com.msa.user.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginRequest {
    @Email
    @Size(min = 2, message = "email should be more than 2 character")
    @NotNull(message = "email cannot be null")
    private String email;

    @Size(min = 8, message = "password should be more than 8 character")
    @NotNull(message = "password cannot be null")
    private String password;
}
