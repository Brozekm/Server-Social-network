package com.brozek.socialnetwork.dos.posts;

import java.time.LocalDateTime;

public interface IPostsDO {
    String getUsername();
    String getEmail();
    String getPostType();
    LocalDateTime getCreatedAt();
    String getMessage();
}
