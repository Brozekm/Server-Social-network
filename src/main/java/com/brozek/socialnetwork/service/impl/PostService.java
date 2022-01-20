package com.brozek.socialnetwork.service.impl;

import com.brozek.socialnetwork.dos.auth.AuthUserDO;
import com.brozek.socialnetwork.dos.auth.EnumAuthRole;
import com.brozek.socialnetwork.dos.posts.EnumPostType;
import com.brozek.socialnetwork.dos.posts.IPostsDO;
import com.brozek.socialnetwork.dos.posts.PostDO;
import com.brozek.socialnetwork.repository.IPostRepository;
import com.brozek.socialnetwork.repository.IUserJpaRepository;
import com.brozek.socialnetwork.service.IAuthenticationService;
import com.brozek.socialnetwork.service.IPostService;
import com.brozek.socialnetwork.vos.post.PostResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService implements IPostService {

    private final IAuthenticationService authenticationService;

    private final IPostRepository postRepository;

    private final IUserJpaRepository userJpaRepository;

    @Override
    public void createPost(String message, EnumPostType postType) throws IllegalAccessException {
        String loggedUser = authenticationService.getUserEmail();
        if (loggedUser == null) {
            log.warn("User is not logged after passing authentication");
            throw new IllegalStateException("User is not logged");
        }

        if (message == null || message.length() == 0){
            throw new IllegalArgumentException("Message is not valid");
        }

        AuthUserDO user = userJpaRepository.findByEmail(loggedUser);
        if (user == null){
            log.error("Logged user ({}) was not found in DB", loggedUser);
            throw new IllegalCallerException("Logged user was not found in DB");
        }


        if (postType == EnumPostType.ANNOUNCEMENT){
            if (user.getRole() != EnumAuthRole.ADMIN){
                log.warn("{} does not have privileges for creating announcements", loggedUser);
                throw new IllegalAccessException("User does not have privileges for announcement");
            }
        }

        PostDO post = new PostDO(user, postType, message);
        postRepository.save(post);
    }

    @Override
    public List<PostResponseVO> getPosts(int offset) {
        String loggedUser = authenticationService.getUserEmail();
        if (loggedUser == null) {
            log.warn("User is not logged after passing authentication");
            throw new IllegalStateException("User is not logged");
        }

        if (offset < 0){
            log.warn("Offset can not be negative number");
            throw new IllegalArgumentException("Offset can not be negative number");
        }

        List<IPostsDO> postsDO = postRepository.getPosts(2, offset);
        var posts = new ArrayList<PostResponseVO>();
        for (var tmpPost: postsDO){
            var post = new PostResponseVO(tmpPost.getMessage(), tmpPost.getType());
            posts.add(post);
        }

        return posts;
    }
}
