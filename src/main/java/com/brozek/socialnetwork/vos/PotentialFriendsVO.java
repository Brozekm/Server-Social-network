package com.brozek.socialnetwork.vos;

public class PotentialFriendsVO {

    private String email;

    private String username;

    public PotentialFriendsVO(String email, String username) {
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
