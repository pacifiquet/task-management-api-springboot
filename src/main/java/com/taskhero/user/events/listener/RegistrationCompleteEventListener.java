package com.taskhero.user.events.listener;

import com.taskhero.email.EmailService;
import com.taskhero.user.events.RegistrationCompleteEvent;
import com.taskhero.user.models.User;
import com.taskhero.user.service.UserService;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Slf4j
@Component
public class RegistrationCompleteEventListener
    implements ApplicationListener<RegistrationCompleteEvent> {
  private final UserService userService;
  private final EmailService emailService;

  @Override
  public void onApplicationEvent(RegistrationCompleteEvent event) {
    // create the verification AuthJwtToken for user with a verify link
    User user = event.getUser();
    String token = UUID.randomUUID().toString();
    userService.saveVerifyToken(user, token);
    String verificationLink = event.getVerifyUrl() + "verify-registration?token=" + token;

    // send Mail to use // send email here
    log.info("Click to link to verify your account: {}", verificationLink);
    String recipientName = String.format("%s %s", user.getFirstName(), user.getLastName());
    String recipient = user.getEmail();
    String subject = "Account Verification";

    try {
      emailService.sendAccountVerifyEmail(recipient, subject, recipientName, verificationLink);
      log.info("Verification email sent");
    } catch (MessagingException | IOException e) {
      log.info("Error send email verification {}", e.getMessage());
    }
  }
}
