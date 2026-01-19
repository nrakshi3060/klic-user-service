package com.klic.user_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klic.user_service.model.MediaResponse;
import com.klic.user_service.model.Post;
import com.klic.user_service.service.MediaService;
import com.klic.user_service.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@ActiveProfiles("test")
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @MockitoBean
    private MediaService mediaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreatePost() throws Exception {
        Post post = new Post();
        post.setUserId(UUID.randomUUID());
        post.setPostDescription("New Post");
        post.setPostType(Post.PostTypeEnum.TEXT);

        when(postService.createPost(any())).thenReturn(post);

        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.postDescription").value("New Post"));
    }

    @Test
    public void testGetAllPosts() throws Exception {
        Post post = new Post();
        post.setId(UUID.randomUUID());
        post.setUserId(UUID.randomUUID());
        post.setPostType(Post.PostTypeEnum.TEXT);

        when(postService.getAllPosts()).thenReturn(Collections.singletonList(post));

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(post.getId().toString()));
    }

    @Test
    public void testGetPostById_Success() throws Exception {
        UUID id = UUID.randomUUID();
        Post post = new Post();
        post.setId(id);
        post.setUserId(UUID.randomUUID());
        post.setPostType(Post.PostTypeEnum.TEXT);

        when(postService.getPostById(id)).thenReturn(Optional.of(post));

        mockMvc.perform(get("/posts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    public void testGetPostById_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(postService.getPostById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/posts/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdatePost_Success() throws Exception {
        UUID id = UUID.randomUUID();
        Post post = new Post();
        post.setUserId(UUID.randomUUID());
        post.setPostDescription("Updated");
        post.setPostType(Post.PostTypeEnum.TEXT);

        when(postService.updatePost(eq(id), any())).thenReturn(Optional.of(post));

        mockMvc.perform(put("/posts/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postDescription").value("Updated"));
    }

    @Test
    public void testDeletePost_Success() throws Exception {
        UUID id = UUID.randomUUID();
        when(postService.deletePost(id)).thenReturn(true);

        mockMvc.perform(delete("/posts/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeletePost_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(postService.deletePost(id)).thenReturn(false);

        mockMvc.perform(delete("/posts/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUploadPostMedia() throws Exception {
        UUID id = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "content".getBytes());
        
        com.klic.user_service.external.resources.Media mediaEntity = new com.klic.user_service.external.resources.Media();
        mediaEntity.setId(UUID.randomUUID());
        mediaEntity.setMediaPath("path/to/file.jpg");

        when(mediaService.uploadMedia(eq(id), any(), any(), any())).thenReturn(mediaEntity);

        mockMvc.perform(multipart("/posts/{id}/media", id)
                .file(file)
                .param("takenLocation", "POINT(30 10)")
                .param("uploadedLocation", "POINT(31 11)"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mediaId").value(mediaEntity.getId().toString()))
                .andExpect(jsonPath("$.mediaPath").value("path/to/file.jpg"));
    }
}
