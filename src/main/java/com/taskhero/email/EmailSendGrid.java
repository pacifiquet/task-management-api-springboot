package com.taskhero.email;

import com.taskhero.user.models.User;
import jakarta.mail.MessagingException;
import java.io.IOException;

public interface EmailSendGrid {
  void sendAccountVerifyEmail(
      String to, String subject, String recipientName, String verificationLink)
      throws MessagingException, IOException;

  boolean resendVerificationEmail(User user, String link, String token);

  boolean passwordResetTokenMail(User user, String link, String token);
}
