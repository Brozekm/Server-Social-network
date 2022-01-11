package com.brozek.socialnetwork.vos;

import java.io.Serializable;

public class JwtResponseVO implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    private String jwttoken;

    private final String email;

    private final String userName;


    public JwtResponseVO(String email, String userName) {
        this.email = email;
        this.userName = userName;
    }


    public String getUserName() {
        return userName;
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
