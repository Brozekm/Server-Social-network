package com.brozek.socialnetwork.vos.post;

import com.brozek.socialnetwork.dos.posts.EnumPostType;
import lombok.Getter;

@Getter
public class PostResponseVO {

    private final String userName;

    private final String email;

    private final String message;

    private final EnumPostType type;


    public PostResponseVO(String message, String type) {
        this.userName = "Karel123";
        this.email = "karel@gmail.com";
        this.message = message;
        this.type = EnumPostType.valueOf(type);
    }
}
