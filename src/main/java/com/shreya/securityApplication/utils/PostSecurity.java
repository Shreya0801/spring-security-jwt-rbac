package com.shreya.securityApplication.utils;


import com.shreya.securityApplication.dto.PostDTO;
import com.shreya.securityApplication.entity.UserEntity;
import com.shreya.securityApplication.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostSecurity {

    private final PostService postService;

    public boolean isOwnerOfPost(Long postId){

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        PostDTO postDTO = postService.getPostById(postId);
        return postDTO.getAuthor().getId().equals(user.getId());
    }
}
