package com.klic.user_service.mapper;

import com.klic.user_service.external.resources.User;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    com.klic.user_service.model.User toApi(User user);

    User toEntity(com.klic.user_service.model.User user);

    default Point map(String value) {
        if (value == null) {
            return null;
        }
        try {
            return (Point) new WKTReader().read(value);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse WKT", e);
        }
    }

    default String map(Point value) {
        if (value == null) {
            return null;
        }
        return new WKTWriter().write(value);
    }
}
