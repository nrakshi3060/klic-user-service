package com.klic.user_service.mapper;

import com.klic.user_service.external.resources.CommentLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, PostCommentMapper.class})
public interface CommentLikeMapper {

    @Mapping(source = "comment.id", target = "commentId")
    @Mapping(source = "user.id", target = "userId")
    com.klic.user_service.model.CommentLike toDto(CommentLike commentLike);

    @Mapping(source = "commentId", target = "comment.id")
    @Mapping(source = "userId", target = "user.id")
    CommentLike toEntity(com.klic.user_service.model.CommentLike commentLike);
}
