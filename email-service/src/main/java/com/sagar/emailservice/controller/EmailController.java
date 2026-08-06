package com.sagar.emailservice.controller;

import com.sagar.emailservice.dto.BookCallRequest;
import com.sagar.emailservice.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@Valid @RequestBody BookCallRequest request) {

        emailService.sendBookCallMail(request);

        return ResponseEntity.ok("Request Received Successfully");

    }

}