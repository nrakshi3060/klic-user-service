package com.klic.user_service.controller;

import com.klic.user_service.api.CommentLikesApi;
import com.klic.user_service.model.CommentLike;
import com.klic.user_service.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CommentLikeController implements CommentLikesApi {

    private final CommentLikeService commentLikeService;

    @Override
    public ResponseEntity<CommentLike> createCommentLike(CommentLike commentLike) {
        CommentLike createdCommentLike = commentLikeService.createCommentLike(commentLike);
        return new ResponseEntity<>(createdCommentLike, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteCommentLike(UUID id) {
        return commentLikeService.deleteCommentLike(id) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<CommentLike>> getAllCommentLikes() {
        return new ResponseEntity<>(commentLikeService.getAllCommentLikes(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CommentLike> getCommentLikeById(UUID id) {
        return commentLikeService.getCommentLikeById(id)
                .map(commentLike -> new ResponseEntity<>(commentLike, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<CommentLike> updateCommentLike(UUID id, CommentLike commentLike) {
        return commentLikeService.updateCommentLike(id, commentLike)
                .map(updatedCommentLike -> new ResponseEntity<>(updatedCommentLike, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
