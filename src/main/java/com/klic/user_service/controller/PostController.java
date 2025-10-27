package com.klic.user_service.controller;

import com.klic.user_service.api.PostsApi;
import com.klic.user_service.model.Post;
import com.klic.user_service.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PostController implements PostsApi {

    private final PostService postService;

    @Override
    public ResponseEntity<Post> createPost(Post post) {
        Post createdPost = postService.createPost(post);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deletePost(UUID id) {
        return postService.deletePost(id) ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<Post>> getAllPosts() {
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Post> getPostById(UUID id) {
        return postService.getPostById(id)
                .map(post -> new ResponseEntity<>(post, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Post> updatePost(UUID id, Post post) {
        return postService.updatePost(id, post)
                .map(updatedPost -> new ResponseEntity<>(updatedPost, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
