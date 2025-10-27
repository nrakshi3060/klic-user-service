package com.klic.user_service.controller;

import com.klic.user_service.api.UsersApi;
import com.klic.user_service.mapper.UserMapper;
import com.klic.user_service.model.User;
import com.klic.user_service.service.UserServiceDb;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class UserController implements UsersApi {

    private final UserServiceDb userServiceDb;

    @Override
    public ResponseEntity<User> createUser(User user) {
        com.klic.user_service.external.resources.User userEntity = UserMapper.INSTANCE.toEntity(user);
        com.klic.user_service.external.resources.User createdUser = userServiceDb.createUser(userEntity);
        return new ResponseEntity<>(UserMapper.INSTANCE.toApi(createdUser), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID id) {
        userServiceDb.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<User>> getAllUsers() {
        List<com.klic.user_service.external.resources.User> users = userServiceDb.getAllUsers();
        List<User> userModels = users.stream().map(UserMapper.INSTANCE::toApi).collect(Collectors.toList());
        return new ResponseEntity<>(userModels, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> getUserById(UUID id) {
        return userServiceDb.getUserById(id)
                .map(user -> new ResponseEntity<>(UserMapper.INSTANCE.toApi(user), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<User> updateUser(UUID id, User user) {
        com.klic.user_service.external.resources.User userEntity = UserMapper.INSTANCE.toEntity(user);
        com.klic.user_service.external.resources.User updatedUser = userServiceDb.updateUser(id, userEntity);
        return new ResponseEntity<>(UserMapper.INSTANCE.toApi(updatedUser), HttpStatus.OK);
    }
}
