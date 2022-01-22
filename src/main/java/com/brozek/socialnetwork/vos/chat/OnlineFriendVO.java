package com.brozek.socialnetwork.vos.chat;

import lombok.Getter;

@Getter
public class OnlineFriendVO {

    private String email;

    private String userName;

    private EnumOnlineStatus status;

    public OnlineFriendVO(String email, String userName, EnumOnlineStatus status) {
        this.email = email;
        this.userName = userName;
        this.status = status;
    }
}
