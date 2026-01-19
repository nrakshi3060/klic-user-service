package com.klic.user_service.mapper;

import com.klic.user_service.external.resources.CommentLike;
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

public class CommentLikeMapperTest {

    private CommentLikeMapper commentLikeMapper;

    @BeforeEach
    public void setUp() {
        commentLikeMapper = Mappers.getMapper(CommentLikeMapper.class);
    }

    @Test
    public void testToDto() {
        CommentLike like = new CommentLike();
        like.setId(UUID.randomUUID());
        
        PostComment comment = new PostComment();
        comment.setId(UUID.randomUUID());
        like.setComment(comment);
        
        User user = new User();
        user.setId(UUID.randomUUID());
        like.setUser(user);

        com.klic.user_service.model.CommentLike dto = commentLikeMapper.toDto(like);

        assertNotNull(dto);
        assertEquals(like.getId(), dto.getId());
        assertEquals(comment.getId(), dto.getCommentId());
        assertEquals(user.getId(), dto.getUserId());
    }

    @Test
    public void testToDto_NullFields() {
        CommentLike like = new CommentLike();
        com.klic.user_service.model.CommentLike dto = commentLikeMapper.toDto(like);
        assertNotNull(dto);
        assertNull(dto.getCommentId());
        assertNull(dto.getUserId());
    }

    @Test
    public void testToDto_Null() {
        assertNull(commentLikeMapper.toDto(null));
    }

    @Test
    public void testToEntity() {
        com.klic.user_service.model.CommentLike dto = new com.klic.user_service.model.CommentLike();
        dto.setCommentId(UUID.randomUUID());
        dto.setUserId(UUID.randomUUID());

        CommentLike like = commentLikeMapper.toEntity(dto);

        assertNotNull(like);
        assertEquals(dto.getCommentId(), like.getComment().getId());
        assertEquals(dto.getUserId(), like.getUser().getId());
    }

    @Test
    public void testToEntity_Null() {
        assertNull(commentLikeMapper.toEntity(null));
    }
}
