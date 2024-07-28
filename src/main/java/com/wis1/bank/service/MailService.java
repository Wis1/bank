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
        mailMessage.setText("Witaj! Kliknij tutaj, aby zweryfikować swoje konto: http://twojaaplikacja.com/verify-email");

        javaMailSender.send(mailMessage);
    }
}
