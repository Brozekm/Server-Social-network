package com.brozek.socialnetwork.vos.post;

import com.brozek.socialnetwork.dos.posts.EnumPostType;
import lombok.Getter;

@Getter
public class PostRequestVO {

    private String message;

    private EnumPostType type;

    public PostRequestVO() {
    }
}
