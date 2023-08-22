package com.taskhero.email;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.taskhero.user.models.User;
import jakarta.mail.MessagingException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements EmailSendGrid {
  private final TemplateEngine templateEngine;

  @Value("${sendgrid.api-key}")
  private String apiKey;

  @Override
  public void sendAccountVerifyEmail(
      String sendTo, String subject, String recipientName, String verificationLink)
      throws MessagingException, IOException {

    Email from = new Email("ppacifiuepaccy@gmail.com");
    Email toEmail = new Email(sendTo);
    SendGrid sendGrid = new SendGrid(apiKey);
    Request request = new Request();

    // create a thymeleaf context and set the dynamic data
    Context context = new Context();
    context.setVariable("recipientName", recipientName);
    context.setVariable("verificationLink", verificationLink);
    //     process the Thymeleaf template with dynamic data
    String htmlContent = templateEngine.process("/email/account-verify-template", context);

    // sendgrid handle
    Content content = new Content("text/html", htmlContent);
    Mail mail = new Mail(from, subject, toEmail, content);
    request.setMethod(Method.POST);
    request.setEndpoint("mail/send");
    request.setBody(mail.build());
    Response response = sendGrid.api(request);

    if (response.getStatusCode() != 202) {
      throw new MessagingException("Failed to send email. Status code");
    }
  }

  @Override
  public boolean resendVerificationEmail(User user, String link, String token) {
    String url = link + "verifyRegistration?token=" + token;
    log.info("Click the link below to verify your account {}:", url);
    return true;
  }

  @Override
  public boolean passwordResetTokenMail(User user, String link, String token) {
    String url = link + "saveResetPassword?token=" + token;
    log.info("Click the link below to reset your password {}", url);
    return true;
  }
}
