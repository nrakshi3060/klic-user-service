package com.klic.user_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klic.user_service.model.CommentLike;
import com.klic.user_service.service.CommentLikeService;
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

@WebMvcTest(CommentLikeController.class)
public class CommentLikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentLikeService commentLikeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateCommentLike() throws Exception {
        CommentLike like = new CommentLike();
        like.setCommentId(UUID.randomUUID());
        like.setUserId(UUID.randomUUID());
        like.setIsLike(true);

        when(commentLikeService.createCommentLike(any())).thenReturn(like);

        mockMvc.perform(post("/comment-likes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(like)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isLike").value(true));
    }

    @Test
    public void testGetAllCommentLikes() throws Exception {
        CommentLike like = new CommentLike();
        like.setId(UUID.randomUUID());
        like.setCommentId(UUID.randomUUID());
        like.setUserId(UUID.randomUUID());

        when(commentLikeService.getAllCommentLikes()).thenReturn(Collections.singletonList(like));

        mockMvc.perform(get("/comment-likes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(like.getId().toString()));
    }

    @Test
    public void testGetCommentLikeById_Success() throws Exception {
        UUID id = UUID.randomUUID();
        CommentLike like = new CommentLike();
        like.setId(id);
        like.setCommentId(UUID.randomUUID());
        like.setUserId(UUID.randomUUID());

        when(commentLikeService.getCommentLikeById(id)).thenReturn(Optional.of(like));

        mockMvc.perform(get("/comment-likes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    public void testGetCommentLikeById_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(commentLikeService.getCommentLikeById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/comment-likes/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateCommentLike_Success() throws Exception {
        UUID id = UUID.randomUUID();
        CommentLike like = new CommentLike();
        like.setCommentId(UUID.randomUUID());
        like.setUserId(UUID.randomUUID());
        like.setIsLike(false);

        when(commentLikeService.updateCommentLike(eq(id), any())).thenReturn(Optional.of(like));

        mockMvc.perform(put("/comment-likes/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(like)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isLike").value(false));
    }

    @Test
    public void testDeleteCommentLike_Success() throws Exception {
        UUID id = UUID.randomUUID();
        when(commentLikeService.deleteCommentLike(id)).thenReturn(true);

        mockMvc.perform(delete("/comment-likes/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteCommentLike_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(commentLikeService.deleteCommentLike(id)).thenReturn(false);

        mockMvc.perform(delete("/comment-likes/{id}", id))
                .andExpect(status().isNotFound());
    }
}
