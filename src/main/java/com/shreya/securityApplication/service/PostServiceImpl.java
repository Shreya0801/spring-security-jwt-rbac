package com.shreya.securityApplication.service;

import com.shreya.securityApplication.dto.PostDTO;
import com.shreya.securityApplication.entity.PostEntity;
import com.shreya.securityApplication.entity.UserEntity;
import com.shreya.securityApplication.exception.ResourceNotFoundException;
import com.shreya.securityApplication.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream().map(postEntity -> modelMapper.map(postEntity, PostDTO.class)).collect(Collectors.toList());
    }

    @Override
    public PostDTO getPostById(Long postId) {
        UserEntity userEntity  = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("user {}",userEntity);

        PostEntity postEntity = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("post with id "+postId+" not found."));
        return modelMapper.map(postEntity, PostDTO.class);
    }

    @Override
    public PostDTO createNewPost(PostDTO inputPost) {

        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        PostEntity postEntity = modelMapper.map(inputPost, PostEntity.class);
        postEntity.setAuthor(user);
        return modelMapper.map(postRepository.save(postEntity), PostDTO.class);
    }

    @Override
    public PostDTO updatePostById(PostDTO inputPost, Long postId) {
        PostEntity olderPost = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("post with id "+postId+" not found."));
        inputPost.setId(postId);
        modelMapper.map(inputPost, olderPost);

        PostEntity savedPostEntity = postRepository.save(olderPost);
        return modelMapper.map(savedPostEntity, PostDTO.class);
    }

    @Override
    public boolean deletePostById(Long postId) {
        if(!postRepository.existsById(postId))
            throw new ResourceNotFoundException("post with id "+postId+" not found.");

        postRepository.deleteById(postId);
        return true;
    }
}
