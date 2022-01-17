package com.brozek.socialnetwork.vos;

public class UserVO {

    private String email;

    private String username;

    public UserVO(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
