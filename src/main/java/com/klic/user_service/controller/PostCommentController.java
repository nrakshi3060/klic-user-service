package com.klic.user_service.controller;

import com.klic.user_service.api.PostCommentsApi;
import com.klic.user_service.model.PostComment;
import com.klic.user_service.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PostCommentController implements PostCommentsApi {

    private final PostCommentService postCommentService;

    @Override
    public ResponseEntity<PostComment> createPostComment(PostComment postComment) {
        PostComment createdPostComment = postCommentService.createPostComment(postComment);
        return new ResponseEntity<>(createdPostComment, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deletePostComment(UUID id) {
        return postCommentService.deletePostComment(id) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<PostComment>> getAllPostComments() {
        return new ResponseEntity<>(postCommentService.getAllPostComments(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PostComment> getPostCommentById(UUID id) {
        return postCommentService.getPostCommentById(id)
                .map(postComment -> new ResponseEntity<>(postComment, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<PostComment> updatePostComment(UUID id, PostComment postComment) {
        return postCommentService.updatePostComment(id, postComment)
                .map(updatedPostComment -> new ResponseEntity<>(updatedPostComment, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
