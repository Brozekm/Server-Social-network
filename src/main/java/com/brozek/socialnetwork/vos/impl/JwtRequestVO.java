package com.brozek.socialnetwork.vos.impl;

import java.io.Serializable;

public class JwtRequestVO implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String email;

    private String password;

    public JwtRequestVO() {
    }

    public JwtRequestVO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
