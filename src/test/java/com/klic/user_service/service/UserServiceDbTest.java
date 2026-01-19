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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
        UUID id = UUID.randomUUID();
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        
        Optional<User> result = userServiceDb.getUserById(id);
        
        assertTrue(result.isPresent());
        verify(userRepository).findById(id);
    }

    @Test
    void testGetAllUsers() {
        userServiceDb.getAllUsers();
        verify(userRepository).findAll();
    }

    @Test
    void testCreateUser() {
        User user = new User();
        userServiceDb.createUser(user);
        verify(userRepository).save(user);
    }

    @Test
    void testUpdateUser_Success() {
        UUID id = UUID.randomUUID();
        User existing = new User();
        User details = new User();
        details.setFirstName("New");
        details.setLastName("Name");
        details.setUsername("newuser");
        details.setEmail("new@example.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        User result = userServiceDb.updateUser(id, details);

        assertEquals("New", result.getFirstName());
        verify(userRepository).save(existing);
    }

    @Test
    void testUpdateUser_NotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userServiceDb.updateUser(id, new User()));
    }

    @Test
    void testDeleteUser() {
        UUID id = UUID.randomUUID();
        userServiceDb.deleteUser(id);
        verify(userRepository).deleteById(id);
    }
}
