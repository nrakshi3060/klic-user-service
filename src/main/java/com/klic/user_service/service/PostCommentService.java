package com.klic.user_service.service;

import com.klic.user_service.external.resources.PostComment;
import com.klic.user_service.external.resources.PostCommentRepository;
import com.klic.user_service.external.resources.PostRepository;
import com.klic.user_service.external.resources.UserRepository;
import com.klic.user_service.mapper.PostCommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostCommentMapper postCommentMapper;

    @Transactional(readOnly = true)
    public List<com.klic.user_service.model.PostComment> getAllPostComments() {
        return postCommentRepository.findAll().stream()
                .map(postCommentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<com.klic.user_service.model.PostComment> getPostCommentById(UUID id) {
        return postCommentRepository.findById(id).map(postCommentMapper::toDto);
    }

    @Transactional
    public com.klic.user_service.model.PostComment createPostComment(com.klic.user_service.model.PostComment postCommentDto) {
        PostComment postComment = postCommentMapper.toEntity(postCommentDto);
        
        if (postCommentDto.getPostId() != null) {
            postComment.setPost(postRepository.getReferenceById(postCommentDto.getPostId()));
        }
        if (postCommentDto.getUserId() != null) {
            postComment.setUser(userRepository.getReferenceById(postCommentDto.getUserId()));
        }
        if (postCommentDto.getParentCommentId() != null) {
            postComment.setParentComment(postCommentRepository.getReferenceById(postCommentDto.getParentCommentId()));
        }

        PostComment savedPostComment = postCommentRepository.save(postComment);
        return postCommentMapper.toDto(savedPostComment);
    }

    @Transactional
    public Optional<com.klic.user_service.model.PostComment> updatePostComment(UUID id, com.klic.user_service.model.PostComment postCommentDto) {
        return postCommentRepository.findById(id)
                .map(existingPostComment -> {
                    PostComment postCommentToUpdate = postCommentMapper.toEntity(postCommentDto);
                    postCommentToUpdate.setId(existingPostComment.getId());
                    postCommentToUpdate.setCreateDateTime(existingPostComment.getCreateDateTime());
                    
                    if (postCommentDto.getPostId() != null) {
                        postCommentToUpdate.setPost(postRepository.getReferenceById(postCommentDto.getPostId()));
                    }
                    if (postCommentDto.getUserId() != null) {
                        postCommentToUpdate.setUser(userRepository.getReferenceById(postCommentDto.getUserId()));
                    }
                    if (postCommentDto.getParentCommentId() != null) {
                        postCommentToUpdate.setParentComment(postCommentRepository.getReferenceById(postCommentDto.getParentCommentId()));
                    }
                    
                    PostComment updatedPostComment = postCommentRepository.save(postCommentToUpdate);
                    return postCommentMapper.toDto(updatedPostComment);
                });
    }

    @Transactional
    public boolean deletePostComment(UUID id) {
        if (postCommentRepository.existsById(id)) {
            postCommentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
