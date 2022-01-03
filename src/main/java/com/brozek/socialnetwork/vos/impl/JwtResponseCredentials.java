package com.brozek.socialnetwork.vos.impl;

import org.springframework.security.core.userdetails.UserDetails;

public class JwtResponseCredentials {

    UserDetails userDetails;

    JwtResponseVO jwtResponseVO;

    public JwtResponseCredentials(UserDetails userDetails, JwtResponseVO jwtResponseVO) {
        this.userDetails = userDetails;
        this.jwtResponseVO = jwtResponseVO;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public JwtResponseVO getJwtResponseVO() {
        return jwtResponseVO;
    }
}
