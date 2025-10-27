package com.klic.user_service.mapper;

import com.klic.user_service.external.resources.PostLike;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, PostMapper.class})
public interface PostLikeMapper {

    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "user.id", target = "userId")
    com.klic.user_service.model.PostLike toDto(PostLike postLike);

    @Mapping(source = "postId", target = "post.id")
    @Mapping(source = "userId", target = "user.id")
    PostLike toEntity(com.klic.user_service.model.PostLike postLike);
}
