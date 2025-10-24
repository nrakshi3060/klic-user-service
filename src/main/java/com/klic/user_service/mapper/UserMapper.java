package com.klic.user_service.mapper;

import com.klic.user_service.external.resources.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    com.klic.user_service.model.User toApi(User user);

    User toEntity(com.klic.user_service.model.User user);
}
