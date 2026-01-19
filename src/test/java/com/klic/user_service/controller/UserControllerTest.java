package com.klic.user_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klic.user_service.model.User;
import com.klic.user_service.service.UserServiceDb;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceDb userServiceDb;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("johndoe");
        user.setEmail("john.doe@example.com");

        com.klic.user_service.external.resources.User userEntity = new com.klic.user_service.external.resources.User();
        userEntity.setId(UUID.randomUUID());
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");

        when(userServiceDb.createUser(any())).thenReturn(userEntity);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userEntity.getId().toString()))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        com.klic.user_service.external.resources.User userEntity = new com.klic.user_service.external.resources.User();
        userEntity.setId(UUID.randomUUID());
        userEntity.setFirstName("John");

        when(userServiceDb.getAllUsers()).thenReturn(Collections.singletonList(userEntity));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userEntity.getId().toString()))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    public void testGetUserById_Success() throws Exception {
        UUID id = UUID.randomUUID();
        com.klic.user_service.external.resources.User userEntity = new com.klic.user_service.external.resources.User();
        userEntity.setId(id);
        userEntity.setFirstName("John");

        when(userServiceDb.getUserById(id)).thenReturn(Optional.of(userEntity));

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    public void testGetUserById_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(userServiceDb.getUserById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateUser() throws Exception {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setFirstName("Updated");
        user.setLastName("User");
        user.setUsername("updated");
        user.setEmail("updated@example.com");

        com.klic.user_service.external.resources.User userEntity = new com.klic.user_service.external.resources.User();
        userEntity.setId(id);
        userEntity.setFirstName("Updated");

        when(userServiceDb.updateUser(eq(id), any())).thenReturn(userEntity);

        mockMvc.perform(put("/users/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(userServiceDb).deleteUser(id);

        mockMvc.perform(delete("/users/{id}", id))
                .andExpect(status().isNoContent());

        verify(userServiceDb).deleteUser(id);
    }
}
