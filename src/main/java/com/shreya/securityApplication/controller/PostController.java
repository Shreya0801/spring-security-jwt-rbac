package com.shreya.securityApplication.controller;


import com.shreya.securityApplication.dto.PostDTO;
import com.shreya.securityApplication.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/posts")
public class PostController {
    private final PostService postService;

    @GetMapping
    @Secured("ROLE_USER")
    public List<PostDTO> getAllPosts(){
        return postService.getAllPosts();
    }

    //@PreAuthorize("hasAnyRole('USER', 'ADMIN') AND hasAuthority('POST_VIEW')")
    @PreAuthorize("@postSecurity.isOwnerOfPost(#postId)")
    @GetMapping(path = "/{postId}")
    public PostDTO getPostById(@PathVariable Long postId){
        return postService.getPostById(postId);
    }

    @PostMapping
    public PostDTO createNewPost(@RequestBody PostDTO inputPost){
        return postService.createNewPost(inputPost);
    }

    @PutMapping("/{postId}")
    public PostDTO updatePostById(@RequestBody PostDTO inputPost, @PathVariable Long postId){
        return postService.updatePostById(inputPost, postId);
    }

    @DeleteMapping("/{postId}")
    public boolean deletePostById(@PathVariable Long postId){
        return postService.deletePostById(postId);
    }

}
