package com.brozek.socialnetwork.controller;

import com.brozek.socialnetwork.dos.posts.EnumPostType;
import com.brozek.socialnetwork.dos.posts.PostDO;
import com.brozek.socialnetwork.service.IPostService;
import com.brozek.socialnetwork.vos.post.PostRequestVO;
import com.brozek.socialnetwork.vos.post.PostResponseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final IPostService postService;

    @PostMapping("/api/posts")
    public ResponseEntity<?> createPost(@RequestBody PostRequestVO postRequestVO){
        try{
            postService.createPost(postRequestVO.getMessage(), postRequestVO.getType());
        } catch (IllegalCallerException e){
            ResponseEntity.internalServerError().body(e.getMessage());
        } catch (IllegalAccessException | IllegalStateException e) {
            ResponseEntity.status(401).body(e.getMessage());
        } catch (IllegalArgumentException e){
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }


    @GetMapping("/api/posts")
    public ResponseEntity<?> getPosts(@RequestParam int offset){
        List<PostResponseVO> posts;
        try{
            posts = postService.getPosts(offset);
        }catch (IllegalStateException | IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(posts);
    }



}
