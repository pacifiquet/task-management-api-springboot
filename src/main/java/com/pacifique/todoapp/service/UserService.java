package com.pacifique.todoapp.service;

import com.pacifique.todoapp.dto.UserRequest;
import com.pacifique.todoapp.dto.UserResponse;
import com.pacifique.todoapp.model.User;
import com.pacifique.todoapp.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public Long registerUser(UserRequest request) {
        return userRepository
            .save(
                User
                    .builder()
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .role(request.getRole())
                    .createdAt(LocalDateTime.now())
                    .build()
            )
            .getId();
    }

    public List<UserResponse> allUsers() {
        return userRepository
            .findAll()
            .stream()
            .map(
                user ->
                    UserResponse
                        .builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .createAt(user.getCreatedAt())
                        .build()
            )
            .toList();
    }

    public UserResponse getUser(Long id) {
        return userRepository
            .findById(id)
            .map(
                user ->
                    UserResponse
                        .builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .createAt(user.getCreatedAt())
                        .build()
            )
            .orElseThrow(
                () ->
                    new NoSuchElementException(
                        String.format("user with id: %s not found", id)
                    )
            );
    }
}
