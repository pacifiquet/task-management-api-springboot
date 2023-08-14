package com.pacifique.todoapp.service;

import com.pacifique.todoapp.dto.UserRequest;
import com.pacifique.todoapp.dto.UserResponse;
import com.pacifique.todoapp.model.User;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
    Long registerUser(UserRequest request, HttpServletRequest http);

    void saveVerifyToken(User user, String token);

    List<UserResponse> listOfUser();

    UserResponse getUser(Long id);

    String verifyUser(String token);
}
