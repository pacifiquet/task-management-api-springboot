package com.pacifique.todoapp.config.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public void sendAccountVerifyEmail(
        String to,
        String subject,
        String recipientName,
        String verificationLink
    )
        throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);

        // create a thymeleaf context and set the dynamic data
        Context context = new Context();
        context.setVariable("recipientName", recipientName);
        context.setVariable("verificationLink", verificationLink);

        // process the Thymeleaf template with dynamic data
        templateEngine.process("email/account-verify-template.html", context);
    }
}
