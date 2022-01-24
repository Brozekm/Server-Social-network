package com.brozek.socialnetwork.vos;

import com.brozek.socialnetwork.dos.auth.EnumAuthUserRole;
import lombok.Getter;

import java.io.Serializable;
import java.util.Set;

@Getter
public class JwtResponseVO implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    private String jwttoken;

    private final String email;

    private final String userName;

    private final Set<EnumAuthUserRole> roles;


    public JwtResponseVO(String email, String userName, Set<EnumAuthUserRole> roles) {
        this.email = email;
        this.userName = userName;
        this.roles = roles;
    }

    public void setToken(String jwttoken) {
        this.jwttoken = jwttoken;
    }
}
