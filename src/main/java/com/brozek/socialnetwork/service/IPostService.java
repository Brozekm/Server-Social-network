package com.brozek.socialnetwork.service;

import com.brozek.socialnetwork.dos.posts.EnumPostType;
import com.brozek.socialnetwork.vos.post.PostResponseVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IPostService {

    @Transactional
    void createPost(String message, EnumPostType postType) throws IllegalAccessException;

    @Transactional
    List<PostResponseVO> getPosts(int offset);

}
