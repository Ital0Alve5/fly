package com.dac.fly.authservice.service;

import com.dac.fly.authservice.dto.email.EmailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {
    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendPasswordEmail(EmailDto emailData) throws MessagingException, IOException {
        String[] nameParts = emailData.nome().split(" ");
        String firstName = nameParts[0];
        String subject = "Requisição de nova senha para " + firstName + "!";
        String template = loadPasswordTemplateEmail();
        template = template.replace("#{nome}", firstName);
        template = template.replace("#{password}", emailData.senha());

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(sender);
        helper.setTo(emailData.email());
        helper.setSubject(subject);
        helper.setText(template, true);

        javaMailSender.send(mimeMessage);
    }

    private String loadPasswordTemplateEmail() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/ConfirmRegister.html");
        return new String(resource.getInputStream().readAllBytes());
    }
}
