package com.taskhero.user.service;

import com.taskhero.user.dto.LoggedInUser;
import com.taskhero.user.dto.PasswordResetRequest;
import com.taskhero.user.dto.UserRegisterRequest;
import com.taskhero.user.dto.UserResponse;
import com.taskhero.user.models.User;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.BiFunction;

public interface UserService {
  String registerUser(UserRegisterRequest request, HttpServletRequest http);

  void saveVerifyToken(User user, String token);

  List<UserResponse> listOfUser();

  UserResponse getUser(Long id);

  String verifyUser(String token);

  String generateNewVerificationToken(String oldToken, HttpServletRequest httpServletRequest);

  String resetPasswordRequest(
      PasswordResetRequest resetRequest, HttpServletRequest httpServletRequest);

  String saveResetPasswordRequest(PasswordResetRequest passwordResetRequest, String token);

  String changePasswordRequest(PasswordResetRequest passwordResetRequest);

  LoggedInUser loggedInUserDetails();

  BiFunction<User, UserResponse, UserResponse> getUserResponse();

  void deleteUser(Long id);
}
