package com.klic.user_service.service;

import com.klic.user_service.external.resources.PostLike;
import com.klic.user_service.external.resources.PostLikeRepository;
import com.klic.user_service.mapper.PostLikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostLikeMapper postLikeMapper;

    @Transactional(readOnly = true)
    public List<com.klic.user_service.model.PostLike> getAllPostLikes() {
        return postLikeRepository.findAll().stream()
                .map(postLikeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<com.klic.user_service.model.PostLike> getPostLikeById(UUID id) {
        return postLikeRepository.findById(id).map(postLikeMapper::toDto);
    }

    @Transactional
    public com.klic.user_service.model.PostLike createPostLike(com.klic.user_service.model.PostLike postLikeDto) {
        PostLike postLike = postLikeMapper.toEntity(postLikeDto);
        PostLike savedPostLike = postLikeRepository.save(postLike);
        return postLikeMapper.toDto(savedPostLike);
    }

    @Transactional
    public Optional<com.klic.user_service.model.PostLike> updatePostLike(UUID id, com.klic.user_service.model.PostLike postLikeDto) {
        return postLikeRepository.findById(id)
                .map(existingPostLike -> {
                    PostLike postLikeToUpdate = postLikeMapper.toEntity(postLikeDto);
                    postLikeToUpdate.setId(existingPostLike.getId());
                    postLikeToUpdate.setCreateDateTime(existingPostLike.getCreateDateTime());
                    PostLike updatedPostLike = postLikeRepository.save(postLikeToUpdate);
                    return postLikeMapper.toDto(updatedPostLike);
                });
    }

    @Transactional
    public boolean deletePostLike(UUID id) {
        if (postLikeRepository.existsById(id)) {
            postLikeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
