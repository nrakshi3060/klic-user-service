package com.klic.user_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klic.user_service.model.PostLike;
import com.klic.user_service.service.PostLikeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
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

@WebMvcTest(PostLikeController.class)
public class PostLikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostLikeService postLikeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreatePostLike() throws Exception {
        PostLike like = new PostLike();
        like.setPostId(UUID.randomUUID());
        like.setUserId(UUID.randomUUID());
        like.setIsLike(true);

        when(postLikeService.createPostLike(any())).thenReturn(like);

        mockMvc.perform(post("/post-likes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(like)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isLike").value(true));
    }

    @Test
    public void testGetAllPostLikes() throws Exception {
        PostLike like = new PostLike();
        like.setId(UUID.randomUUID());
        like.setPostId(UUID.randomUUID());
        like.setUserId(UUID.randomUUID());

        when(postLikeService.getAllPostLikes()).thenReturn(Collections.singletonList(like));

        mockMvc.perform(get("/post-likes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(like.getId().toString()));
    }

    @Test
    public void testGetPostLikeById_Success() throws Exception {
        UUID id = UUID.randomUUID();
        PostLike like = new PostLike();
        like.setId(id);
        like.setPostId(UUID.randomUUID());
        like.setUserId(UUID.randomUUID());

        when(postLikeService.getPostLikeById(id)).thenReturn(Optional.of(like));

        mockMvc.perform(get("/post-likes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    public void testGetPostLikeById_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(postLikeService.getPostLikeById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/post-likes/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdatePostLike_Success() throws Exception {
        UUID id = UUID.randomUUID();
        PostLike like = new PostLike();
        like.setPostId(UUID.randomUUID());
        like.setUserId(UUID.randomUUID());
        like.setIsLike(false);

        when(postLikeService.updatePostLike(eq(id), any())).thenReturn(Optional.of(like));

        mockMvc.perform(put("/post-likes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(like)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isLike").value(false));
    }

    @Test
    public void testDeletePostLike_Success() throws Exception {
        UUID id = UUID.randomUUID();
        when(postLikeService.deletePostLike(id)).thenReturn(true);

        mockMvc.perform(delete("/post-likes/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeletePostLike_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(postLikeService.deletePostLike(id)).thenReturn(false);

        mockMvc.perform(delete("/post-likes/{id}", id))
                .andExpect(status().isNotFound());
    }
}
