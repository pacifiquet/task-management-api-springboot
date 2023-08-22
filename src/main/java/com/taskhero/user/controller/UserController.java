package com.taskhero.user.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.taskhero.user.dto.LoggedInUser;
import com.taskhero.user.dto.PasswordResetRequest;
import com.taskhero.user.dto.UserRegisterRequest;
import com.taskhero.user.dto.UserResponse;
import com.taskhero.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("api/v1/users")
@Tag(name = "User Controller")
public class UserController {
  private final UserService userService;

  @Operation(summary = "user registration")
  @PostMapping("/register")
  public ResponseEntity<String> registerUser(
      @RequestBody @Validated UserRegisterRequest request, HttpServletRequest http) {
    return ResponseEntity.status(CREATED).body(userService.registerUser(request, http));
  }

  @ResponseStatus(CREATED)
  @Operation(summary = "user change password")
  @PostMapping("/change-password")
  public String changePassword(@RequestBody PasswordResetRequest passwordResetRequest) {
    return userService.changePasswordRequest(passwordResetRequest);
  }

  @PostMapping("/reset-password")
  @Operation(summary = "reset password")
  @ResponseStatus(CREATED)
  public String resetPassword(
      @RequestBody PasswordResetRequest passwordResetRequest,
      HttpServletRequest httpServletRequest) {
    return userService.resetPasswordRequest(passwordResetRequest, httpServletRequest);
  }

  @PostMapping("/save-reset-password")
  @Operation(summary = "save reset password")
  @ResponseStatus(CREATED)
  public String saveResetPassword(
      @RequestBody PasswordResetRequest passwordResetRequest, @RequestParam("token") String token) {
    return userService.saveResetPasswordRequest(passwordResetRequest, token);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "delete user")
  @ResponseStatus(NO_CONTENT)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void deleteUser(@PathVariable("id") Long id) {
    userService.deleteUser(id);
  }

  @GetMapping("/verify-registration")
  @Operation(summary = "user verification")
  @ResponseStatus(OK)
  public String verifyUser(@RequestParam("token") String token) {
    return userService.verifyUser(token);
  }

  @GetMapping("/resend-verify-token")
  @Operation(summary = "user resend verification token")
  @ResponseStatus(OK)
  public String resendVerificationToken(
      @RequestParam("token") String oldToken, HttpServletRequest httpServletRequest) {
    return userService.generateNewVerificationToken(oldToken, httpServletRequest);
  }

  @GetMapping
  @Operation(summary = "get list user")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "user List",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserResponse.class)),
            }),
        @ApiResponse(responseCode = "500", description = "server error", content = @Content),
      })
  @ResponseStatus(OK)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public List<UserResponse> getAllUsers() {
    return userService.listOfUser();
  }

  @GetMapping("/{id}")
  @Operation(summary = "get user by id")
  @ResponseStatus(OK)
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public UserResponse getUser(@PathVariable Long id) {
    return userService.getUser(id);
  }

  @GetMapping("/logged-user-details")
  @ResponseStatus(OK)
  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_MANAGER','ROLE_TEAM_MEMBER')")
  @Operation(summary = "logged in user details")
  public LoggedInUser getLoginUser() {
    return userService.loggedInUserDetails();
  }
}
