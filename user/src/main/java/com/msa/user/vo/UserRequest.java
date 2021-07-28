package com.msa.user.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserRequest {
    @Email
    @Size(min = 2, message = "email은 2글자 이상이 되어야 합니다.")
    @NotNull(message = "emial should not be null")
    private String email;

    @Size(min = 2, message = "name은 2글자 이상이 되어야 합니다.")
    @NotNull(message = "name should not be null")
    private String name;

    @Size(min = 8, message = "password는 8글자 이상이 되어야 합니다.")
    @NotNull(message = "password should not be null")
    private String pwd;
}
