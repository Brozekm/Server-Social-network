package com.brozek.socialnetwork.vos.impl;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class RegisterCredentialsVO {

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String firstName;

    @NotNull
    private String surname;

    public RegisterCredentialsVO() {
    }

    public RegisterCredentialsVO(String email, String password, String firstName, String surname) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }
}
