package com.klic.user_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klic.user_service.model.PostComment;
import com.klic.user_service.service.PostCommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
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

@WebMvcTest(PostCommentController.class)
@ActiveProfiles("test")
public class PostCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostCommentService postCommentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreatePostComment() throws Exception {
        PostComment comment = new PostComment();
        comment.setPostId(UUID.randomUUID());
        comment.setUserId(UUID.randomUUID());
        comment.setCommentText("Comment");

        when(postCommentService.createPostComment(any())).thenReturn(comment);

        mockMvc.perform(post("/post-comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.commentText").value("Comment"));
    }

    @Test
    public void testGetAllPostComments() throws Exception {
        PostComment comment = new PostComment();
        comment.setId(UUID.randomUUID());
        comment.setPostId(UUID.randomUUID());
        comment.setUserId(UUID.randomUUID());
        comment.setCommentText("Comment");

        when(postCommentService.getAllPostComments()).thenReturn(Collections.singletonList(comment));

        mockMvc.perform(get("/post-comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(comment.getId().toString()));
    }

    @Test
    public void testGetPostCommentById_Success() throws Exception {
        UUID id = UUID.randomUUID();
        PostComment comment = new PostComment();
        comment.setId(id);
        comment.setPostId(UUID.randomUUID());
        comment.setUserId(UUID.randomUUID());
        comment.setCommentText("Comment");

        when(postCommentService.getPostCommentById(id)).thenReturn(Optional.of(comment));

        mockMvc.perform(get("/post-comments/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    public void testGetPostCommentById_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(postCommentService.getPostCommentById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/post-comments/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdatePostComment_Success() throws Exception {
        UUID id = UUID.randomUUID();
        PostComment comment = new PostComment();
        comment.setPostId(UUID.randomUUID());
        comment.setUserId(UUID.randomUUID());
        comment.setCommentText("Updated");

        when(postCommentService.updatePostComment(eq(id), any())).thenReturn(Optional.of(comment));

        mockMvc.perform(put("/post-comments/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentText").value("Updated"));
    }

    @Test
    public void testDeletePostComment_Success() throws Exception {
        UUID id = UUID.randomUUID();
        when(postCommentService.deletePostComment(id)).thenReturn(true);

        mockMvc.perform(delete("/post-comments/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeletePostComment_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(postCommentService.deletePostComment(id)).thenReturn(false);

        mockMvc.perform(delete("/post-comments/{id}", id))
                .andExpect(status().isNotFound());
    }
}
