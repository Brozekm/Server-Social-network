package com.brozek.socialnetwork.vos.post;

import com.brozek.socialnetwork.dos.posts.EnumPostType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseVO {

    private final String userName;

    private final String email;

    private final EnumPostType postType;

    private LocalDateTime createdAt;

    private final String message;


    public PostResponseVO(String userName, String email,  String type, LocalDateTime createdAt, String message) {
        this.userName = userName;
        this.email = email;
        this.postType = EnumPostType.valueOf(type);
        this.createdAt = createdAt;
        this.message = message;
    }
}
