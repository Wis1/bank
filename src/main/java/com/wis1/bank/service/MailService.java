package com.wis1.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    public void sendVerificationEmail(String recipientEmail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientEmail);
        mailMessage.setSubject("Email Verification");
        mailMessage.setText("Hello! Click here to verify your account and login: http://localhost/8080");

        javaMailSender.send(mailMessage);
    }
}
