package com.sagar.emailservice.service;

import com.sagar.emailservice.dto.BookCallRequest;

public interface EmailService {

    void sendBookCallMail(BookCallRequest request);

}