package com.brozek.socialnetwork.vos;

import com.brozek.socialnetwork.dos.auth.EnumAuthRole;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class JwtResponseVO implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    private String jwttoken;

    private final String email;

    private final String userName;

    private final EnumAuthRole role;


    public JwtResponseVO(String email, String userName, EnumAuthRole role) {
        this.email = email;
        this.userName = userName;
        this.role = role;
    }

    public void setToken(String jwttoken) {
        this.jwttoken = jwttoken;
    }
}
