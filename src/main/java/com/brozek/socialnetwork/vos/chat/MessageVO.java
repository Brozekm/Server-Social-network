package com.brozek.socialnetwork.vos.chat;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class MessageVO {

    @NotNull
    String targetUser;

    @NotNull
    String message;

    public MessageVO(String targetUser, String message) {
        this.targetUser = targetUser;
        this.message = message;
    }

    public MessageVO() {
    }


}
