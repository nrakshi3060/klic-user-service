package com.klic.user_service.mapper;

import com.klic.user_service.external.resources.PostComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, PostMapper.class})
public interface PostCommentMapper {

    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "parentComment.id", target = "parentCommentId")
    com.klic.user_service.model.PostComment toDto(PostComment postComment);

    @Mapping(source = "postId", target = "post.id")
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "parentCommentId", target = "parentComment.id")
    PostComment toEntity(com.klic.user_service.model.PostComment postComment);
}
