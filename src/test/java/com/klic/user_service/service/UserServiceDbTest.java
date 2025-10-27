package com.klic.user_service.service;

import com.klic.user_service.external.resources.User;
import com.klic.user_service.external.resources.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UserServiceDbTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceDb userServiceDb;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserById() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userServiceDb.getUserById(userId);

        assertEquals(user, result.get());
    }
}
