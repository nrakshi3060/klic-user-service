package com.klic.user_service.mapper;

import com.klic.user_service.external.resources.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PostMapper {

    @Mapping(source = "user.id", target = "userId")
    com.klic.user_service.model.Post toDto(Post post);

    @Mapping(source = "userId", target = "user.id")
    Post toEntity(com.klic.user_service.model.Post post);
}
