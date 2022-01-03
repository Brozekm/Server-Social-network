package com.brozek.socialnetwork.vos.impl;

import java.io.Serializable;

public class JwtResponseVO implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    private String jwttoken;

    private final String email;

    private final String firstName;

    private final String surname;


    public JwtResponseVO(String email, String firstName, String surname) {
        this.email = email;
        this.firstName = firstName;
        this.surname = surname;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public void setToken(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public String getToken() {
        return jwttoken;
    }
}
