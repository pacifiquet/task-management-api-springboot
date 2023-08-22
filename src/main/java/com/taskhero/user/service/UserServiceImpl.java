package com.taskhero.user.service;

import com.taskhero.email.EmailSendGrid;
import com.taskhero.user.dto.LoggedInUser;
import com.taskhero.user.dto.PasswordResetRequest;
import com.taskhero.user.dto.UserRegisterRequest;
import com.taskhero.user.dto.UserResponse;
import com.taskhero.user.events.RegistrationCompleteEvent;
import com.taskhero.user.models.PasswordResetToken;
import com.taskhero.user.models.Permission;
import com.taskhero.user.models.Role;
import com.taskhero.user.models.User;
import com.taskhero.user.models.VerificationToken;
import com.taskhero.user.repository.PasswordResetRepository;
import com.taskhero.user.repository.UserRepository;
import com.taskhero.user.repository.VerificationTokenRepository;
import com.taskhero.user.service.authorservice.UserDetailsImpl;
import com.taskhero.utils.Time;
import com.taskhero.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final VerificationTokenRepository verificationTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final ApplicationEventPublisher publisher;
  private final EmailSendGrid emailService;
  private final PasswordResetRepository passwordResetRepository;
  private final IUserRoleAndPermissionHandler userRoleAndPermissionHandler;

  @Override
  @Transactional
  public String registerUser(UserRegisterRequest request, HttpServletRequest http) {
    User user =
        User.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .createdAt(Time.currentDateTime())
            .build();
    userRoleAndPermissionHandler.extractRoles(request.getRoles(), user);
    userRoleAndPermissionHandler.extractPermissions(request.getPermissions(), user);
    // registration event started
    User savedUser = userRepository.save(user);
    publisher.publishEvent(new RegistrationCompleteEvent(savedUser, Utils.generateUrl(http)));
    return "Check your email to verify your account: %s".formatted(savedUser.getEmail());
  }

  @Override
  public void saveVerifyToken(User user, String token) {
    VerificationToken verificationToken =
        VerificationToken.builder()
            .user(user)
            .expirationTime(Utils.calculateTokenExpirationDate())
            .token(token)
            .build();
    verificationTokenRepository.save(verificationToken);
  }

  @Override
  @Transactional
  public String verifyUser(String token) {
    VerificationToken verificationToken =
        verificationTokenRepository
            .findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

    User user = verificationToken.getUser();
    if (user.isEnabled()) return "user is verified";
    Calendar instance = Calendar.getInstance();
    if (verificationToken.getExpirationTime().getTime() - instance.getTime().getTime() <= 0) {
      verificationTokenRepository.delete(verificationToken);
      return "expired token";
    }
    user.setEnabled(true);
    userRepository.save(user);
    return "User verifies Successfully";
  }

  @Override
  @Transactional
  public String generateNewVerificationToken(
      String oldToken, HttpServletRequest httpServletRequest) {
    VerificationToken verificationToken =
        verificationTokenRepository
            .findByToken(oldToken)
            .orElseThrow(() -> new IllegalStateException("token not found"));
    verificationToken.setToken(UUID.randomUUID().toString());

    VerificationToken savedVerificationToken = verificationTokenRepository.save(verificationToken);

    boolean resentVerificationEmail =
        emailService.resendVerificationEmail(
            savedVerificationToken.getUser(),
            Utils.generateUrl(httpServletRequest),
            savedVerificationToken.getToken());
    return resentVerificationEmail ? "Verification Link Sent" : "Action failed";
  }

  @Override
  @Transactional
  public String resetPasswordRequest(
      PasswordResetRequest resetRequest, HttpServletRequest httpServletRequest) {
    User user =
        userRepository
            .findByEmail(resetRequest.getEmail())
            .orElseThrow(() -> new IllegalStateException("email not found"));

    PasswordResetToken savedRestToken =
        passwordResetRepository.save(
            PasswordResetToken.builder()
                .expirationTime(Utils.calculateTokenExpirationDate())
                .passwordToken(UUID.randomUUID().toString())
                .user(user)
                .build());

    boolean resetTokenMail =
        emailService.passwordResetTokenMail(
            savedRestToken.getUser(),
            Utils.generateUrl(httpServletRequest),
            savedRestToken.getPasswordToken());
    return resetTokenMail ? "Reset Password Mail Send!" : "Reset your password failed!";
  }

  @Override
  public String saveResetPasswordRequest(PasswordResetRequest passwordResetRequest, String token) {
    PasswordResetToken passwordResetToken =
        passwordResetRepository
            .findByPasswordToken(token)
            .orElseThrow(() -> new IllegalStateException("token not found"));
    User user = passwordResetToken.getUser();

    if (user != null) {
      user.setPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
      userRepository.save(user);
      return "Password Reset Successfully";
    }
    return "Invalid user";
  }

  @Override
  public String changePasswordRequest(PasswordResetRequest passwordResetRequest) {
    User user =
        userRepository
            .findByEmail(passwordResetRequest.getEmail())
            .orElseThrow(() -> new IllegalStateException("user not found"));

    boolean matches =
        passwordEncoder.matches(passwordResetRequest.getOldPassword(), user.getPassword());
    if (!matches) {
      return "Invalid Old Password";
    }
    user.setPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
    userRepository.save(user);
    return "Password Changed Successfully";
  }

  @Override
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }

  public List<UserResponse> listOfUser() {
    return userRepository.findAll().stream()
        .map(user -> getUserResponse().apply(user, new UserResponse()))
        .toList();
  }

  @Override
  public UserResponse getUser(Long id) {
    return getUserResponse()
        .apply(
            userRepository
                .findById(id)
                .orElseThrow(
                    () ->
                        new NoSuchElementException(
                            String.format("user with id: %s not found", id))),
            new UserResponse());
  }

  @Override
  public LoggedInUser loggedInUserDetails() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
    return LoggedInUser.builder()
        .authorities(principal.getAuthorities())
        .userId(principal.getId())
        .email(principal.getEmail())
        .firstName(principal.getFirstName())
        .createAt(principal.getCreatedAt().toString())
        .lastName(principal.getLastName())
        .build();
  }

  @Override
  public BiFunction<User, UserResponse, UserResponse> getUserResponse() {
    return ((user, userResponse) ->
        UserResponse.builder()
            .userId(user.getUserId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .enabled(user.isEnabled())
            .createAt(user.getCreatedAt().toString())
            .permissions(listPermission().apply(user.getUserPermissions()))
            .roles(listRole().apply(user.getRoles()))
            .build());
  }

  private static Function<Set<Role>, List<String>> listRole() {
    return (role -> role.stream().map(r -> r.getUserRole().name()).toList());
  }

  private static Function<Set<Permission>, List<String>> listPermission() {
    return (role -> role.stream().map(r -> r.getUserPermissions().name()).toList());
  }
}
