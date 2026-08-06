package com.sagar.emailservice.service.impl;

import com.sagar.emailservice.dto.BookCallRequest;
import com.sagar.emailservice.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Override
    public void sendBookCallMail(BookCallRequest request) {

        try {

            Context context = new Context();

            context.setVariable("name", request.getName());
            context.setVariable("phone", request.getPhone());
            context.setVariable("email", request.getEmail());
            context.setVariable("service", request.getService());

            String html = templateEngine.process("book-call", context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(mailUsername);
            helper.setTo(adminEmail);
            helper.setSubject("📞 New Book a Call Request");
            helper.setText(html, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send email", e);
        }
    }
}