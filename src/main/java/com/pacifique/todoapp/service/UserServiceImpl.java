package com.pacifique.todoapp.service;

import com.pacifique.todoapp.config.utils.Utils;
import com.pacifique.todoapp.config.utils.events.RegistrationCompleteEvent;
import com.pacifique.todoapp.config.utils.time.Time;
import com.pacifique.todoapp.dto.UserRequest;
import com.pacifique.todoapp.dto.UserResponse;
import com.pacifique.todoapp.model.User;
import com.pacifique.todoapp.model.VerificationToken;
import com.pacifique.todoapp.repository.UserRepository;
import com.pacifique.todoapp.repository.VerificationTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;

    @Override
    public Long registerUser(UserRequest request, HttpServletRequest http) {
        User user = userRepository.save(
            User
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .role(request.getRole())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(Time.currentDateTime())
                .build()
        );
        // registration event started
        publisher.publishEvent(
            new RegistrationCompleteEvent(user, Utils.verifyUrl(http))
        );

        return user.getUserId();
    }

    @Override
    @Transactional
    public String verifyUser(String token) {
        VerificationToken verificationToken = tokenRepository
            .findByToken(token)
            .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        User user = verificationToken.getUser();
        if (user.isEnabled()) return "user is verified";
        Calendar instance = Calendar.getInstance();
        if (
            verificationToken.getExpirationTime().getTime() -
            instance.getTime().getTime() <=
            0
        ) {
            tokenRepository.delete(verificationToken);
            return "expired token";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "User verifies Successfully";
    }

    @Override
    public void saveVerifyToken(User user, String token) {
        VerificationToken verificationToken = VerificationToken
            .builder()
            .user(user)
            .expirationTime(Utils.calculateTokenExpirationDate())
            .token(token)
            .build();
        tokenRepository.save(verificationToken);
    }

    public List<UserResponse> listOfUser() {
        return userRepository
            .findAll()
            .stream()
            .map(
                user ->
                    UserResponse
                        .builder()
                        .userId(user.getUserId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .enabled(user.isEnabled())
                        .createAt(user.getCreatedAt())
                        .build()
            )
            .toList();
    }

    @Override
    public UserResponse getUser(Long id) {
        return userRepository
            .findById(id)
            .map(
                user ->
                    UserResponse
                        .builder()
                        .userId(user.getUserId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .enabled(user.isEnabled())
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
