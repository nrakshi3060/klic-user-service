package com.klic.user_service.service;

import com.klic.user_service.external.resources.Post;
import com.klic.user_service.external.resources.PostRepository;
import com.klic.user_service.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public List<com.klic.user_service.model.Post> getAllPosts() {
        return postRepository.findAll().stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<com.klic.user_service.model.Post> getPostById(UUID id) {
        return postRepository.findById(id).map(postMapper::toDto);
    }

    @Transactional
    public com.klic.user_service.model.Post createPost(com.klic.user_service.model.Post postDto) {
        Post post = postMapper.toEntity(postDto);
        Post savedPost = postRepository.save(post);
        return postMapper.toDto(savedPost);
    }

    @Transactional
    public Optional<com.klic.user_service.model.Post> updatePost(UUID id, com.klic.user_service.model.Post postDto) {
        return postRepository.findById(id)
                .map(existingPost -> {
                    Post postToUpdate = postMapper.toEntity(postDto);
                    postToUpdate.setId(existingPost.getId());
                    postToUpdate.setCreateDateTime(existingPost.getCreateDateTime());
                    Post updatedPost = postRepository.save(postToUpdate);
                    return postMapper.toDto(updatedPost);
                });
    }

    @Transactional
    public boolean deletePost(UUID id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
