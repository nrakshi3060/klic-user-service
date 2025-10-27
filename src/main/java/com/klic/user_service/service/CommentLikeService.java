package com.klic.user_service.service;

import com.klic.user_service.external.resources.CommentLike;
import com.klic.user_service.external.resources.CommentLikeRepository;
import com.klic.user_service.mapper.CommentLikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentLikeMapper commentLikeMapper;

    @Transactional(readOnly = true)
    public List<com.klic.user_service.model.CommentLike> getAllCommentLikes() {
        return commentLikeRepository.findAll().stream()
                .map(commentLikeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<com.klic.user_service.model.CommentLike> getCommentLikeById(UUID id) {
        return commentLikeRepository.findById(id).map(commentLikeMapper::toDto);
    }

    @Transactional
    public com.klic.user_service.model.CommentLike createCommentLike(com.klic.user_service.model.CommentLike commentLikeDto) {
        CommentLike commentLike = commentLikeMapper.toEntity(commentLikeDto);
        CommentLike savedCommentLike = commentLikeRepository.save(commentLike);
        return commentLikeMapper.toDto(savedCommentLike);
    }

    @Transactional
    public Optional<com.klic.user_service.model.CommentLike> updateCommentLike(UUID id, com.klic.user_service.model.CommentLike commentLikeDto) {
        return commentLikeRepository.findById(id)
                .map(existingCommentLike -> {
                    CommentLike commentLikeToUpdate = commentLikeMapper.toEntity(commentLikeDto);
                    commentLikeToUpdate.setId(existingCommentLike.getId());
                    commentLikeToUpdate.setCreateDateTime(existingCommentLike.getCreateDateTime());
                    CommentLike updatedCommentLike = commentLikeRepository.save(commentLikeToUpdate);
                    return commentLikeMapper.toDto(updatedCommentLike);
                });
    }

    @Transactional
    public boolean deleteCommentLike(UUID id) {
        if (commentLikeRepository.existsById(id)) {
            commentLikeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
