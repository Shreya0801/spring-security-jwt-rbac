package com.shreya.securityApplication.service;

import com.shreya.securityApplication.dto.PostDTO;

import java.util.List;

public interface PostService {

    List<PostDTO> getAllPosts();

    PostDTO getPostById(Long postId);

    PostDTO createNewPost(PostDTO inputPost);

    PostDTO updatePostById(PostDTO inputPost, Long postId );

    boolean deletePostById(Long postId);


}
