package com.brozek.socialnetwork.vos.impl;

import java.io.Serializable;

public class JwtResponseVO implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    private final String jwttoken;

    public JwtResponseVO(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public String getToken() {
        return jwttoken;
    }
}
