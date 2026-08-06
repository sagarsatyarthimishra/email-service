package com.sagar.emailservice.service.impl;

import com.sagar.emailservice.dto.BookCallRequest;
import com.sagar.emailservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final TemplateEngine templateEngine;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${resend.api.key}")
    private String resendApiKey;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public void sendBookCallMail(BookCallRequest request) {

        Context context = new Context();

        context.setVariable("name", request.getName());
        context.setVariable("phone", request.getPhone());
        context.setVariable("email", request.getEmail());
        context.setVariable("service", request.getService());

        String html = templateEngine.process("book-call", context);

        String json = """
                {
                  "from":"Book A Call <onboarding@resend.dev>",
                  "to":["%s"],
                  "subject":"📞 New Book A Call Request",
                  "html":%s
                }
                """.formatted(
                adminEmail,
                "\"" + html.replace("\"", "\\\"")
                        .replace("\n", "")
                        .replace("\r", "") + "\""
        );

        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );

        Request httpRequest = new Request.Builder()
                .url("https://api.resend.com/emails")
                .addHeader("Authorization", "Bearer " + resendApiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(httpRequest).execute()) {

            if (!response.isSuccessful()) {
                throw new RuntimeException("Resend Error : " + response.body().string());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}