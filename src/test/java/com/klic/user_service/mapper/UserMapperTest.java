package com.klic.user_service.mapper;

import com.klic.user_service.external.resources.User;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserMapperTest {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Test
    public void testToApi() throws Exception {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("johndoe");
        user.setEmail("john.doe@example.com");
        
        String wkt = "POINT(30 10)";
        user.setSignupLocation((Point) new WKTReader().read(wkt));

        com.klic.user_service.model.User apiUser = userMapper.toApi(user);

        assertNotNull(apiUser);
        assertEquals(user.getId(), apiUser.getId());
        assertEquals(user.getFirstName(), apiUser.getFirstName());
        assertEquals(user.getLastName(), apiUser.getLastName());
        assertEquals(user.getUsername(), apiUser.getUsername());
        assertEquals(user.getEmail(), apiUser.getEmail());
        assertEquals("POINT (30 10)", apiUser.getSignupLocation());
    }

    @Test
    public void testToEntity() {
        com.klic.user_service.model.User apiUser = new com.klic.user_service.model.User();
        apiUser.setFirstName("Jane");
        apiUser.setLastName("Doe");
        apiUser.setUsername("janedoe");
        apiUser.setEmail("jane.doe@example.com");
        apiUser.setSignupLocation("POINT (40 20)");

        User user = userMapper.toEntity(apiUser);

        assertNotNull(user);
        assertEquals(apiUser.getFirstName(), user.getFirstName());
        assertEquals(apiUser.getLastName(), user.getLastName());
        assertEquals(apiUser.getUsername(), user.getUsername());
        assertEquals(apiUser.getEmail(), user.getEmail());
        assertNotNull(user.getSignupLocation());
        assertEquals(40.0, user.getSignupLocation().getX());
        assertEquals(20.0, user.getSignupLocation().getY());
    }

    @Test
    public void testToApi_Null() {
        assertNull(userMapper.toApi(null));
    }

    @Test
    public void testToEntity_Null() {
        assertNull(userMapper.toEntity(null));
    }

    @Test
    public void testMapStringNull() {
        assertNull(userMapper.map((String) null));
    }

    @Test
    public void testMapPointNull() {
        assertNull(userMapper.map((Point) null));
    }

    @Test
    public void testMapInvalidWkt() {
        assertThrows(RuntimeException.class, () -> userMapper.map("INVALID"));
    }
}
