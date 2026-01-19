package com.klic.user_service.mapper;

import com.klic.user_service.external.resources.Post;
import com.klic.user_service.external.resources.PostComment;
import com.klic.user_service.external.resources.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PostCommentMapperTest {

    private PostCommentMapper postCommentMapper;

    @BeforeEach
    public void setUp() {
        postCommentMapper = Mappers.getMapper(PostCommentMapper.class);
    }

    @Test
    public void testToDto() {
        PostComment comment = new PostComment();
        comment.setId(UUID.randomUUID());
        comment.setCommentText("Nice post!");
        
        Post post = new Post();
        post.setId(UUID.randomUUID());
        comment.setPost(post);
        
        User user = new User();
        user.setId(UUID.randomUUID());
        comment.setUser(user);

        com.klic.user_service.model.PostComment dto = postCommentMapper.toDto(comment);

        assertNotNull(dto);
        assertEquals(comment.getId(), dto.getId());
        assertEquals(post.getId(), dto.getPostId());
        assertEquals(user.getId(), dto.getUserId());
        assertEquals(comment.getCommentText(), dto.getCommentText());
    }

    @Test
    public void testToDto_NullFields() {
        PostComment comment = new PostComment();
        com.klic.user_service.model.PostComment dto = postCommentMapper.toDto(comment);
        assertNotNull(dto);
        assertNull(dto.getPostId());
        assertNull(dto.getUserId());
    }

    @Test
    public void testToDto_Null() {
        assertNull(postCommentMapper.toDto(null));
    }

    @Test
    public void testToEntity() {
        com.klic.user_service.model.PostComment dto = new com.klic.user_service.model.PostComment();
        dto.setPostId(UUID.randomUUID());
        dto.setUserId(UUID.randomUUID());
        dto.setCommentText("Testing...");

        PostComment comment = postCommentMapper.toEntity(dto);

        assertNotNull(comment);
        assertNull(comment.getPost());
        assertNull(comment.getUser());
        assertNull(comment.getParentComment());
        assertEquals(dto.getCommentText(), comment.getCommentText());
    }

    @Test
    public void testToEntity_Null() {
        assertNull(postCommentMapper.toEntity(null));
    }
}
