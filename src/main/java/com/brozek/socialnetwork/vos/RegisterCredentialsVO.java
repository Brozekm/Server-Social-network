package com.brozek.socialnetwork.vos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class RegisterCredentialsVO {

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String userName;

    public RegisterCredentialsVO(String email, String password, String userName) {
        this.email = email;
        this.password = password;
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }
}
