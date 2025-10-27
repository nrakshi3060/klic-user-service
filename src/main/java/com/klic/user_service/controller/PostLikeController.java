package com.klic.user_service.controller;

import com.klic.user_service.api.PostLikesApi;
import com.klic.user_service.model.PostLike;
import com.klic.user_service.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PostLikeController implements PostLikesApi {

    private final PostLikeService postLikeService;

    @Override
    public ResponseEntity<PostLike> createPostLike(PostLike postLike) {
        PostLike createdPostLike = postLikeService.createPostLike(postLike);
        return new ResponseEntity<>(createdPostLike, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deletePostLike(UUID id) {
        return postLikeService.deletePostLike(id) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<PostLike>> getAllPostLikes() {
        return new ResponseEntity<>(postLikeService.getAllPostLikes(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PostLike> getPostLikeById(UUID id) {
        return postLikeService.getPostLikeById(id)
                .map(postLike -> new ResponseEntity<>(postLike, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<PostLike> updatePostLike(UUID id, PostLike postLike) {
        return postLikeService.updatePostLike(id, postLike)
                .map(updatedPostLike -> new ResponseEntity<>(updatedPostLike, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
