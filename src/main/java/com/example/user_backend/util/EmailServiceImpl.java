package com.example.user_backend.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender mailSender;

	@Override
	public void sendWelcomeEmail(String to, String name) {
		try {
			
			ClassPathResource resource = new ClassPathResource("template/welcome-email.html");
			String htmlContent = Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);

			htmlContent = htmlContent.replace("{NAME}", name).replace("{EMAIL}", to);

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
			helper.setTo(to);
			helper.setSubject("Welcome to Evilan Application");
			helper.setText(htmlContent, true); 

			mailSender.send(message);

		} catch (IOException e) {
			throw new RuntimeException("Failed to load email template: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
		}
	}
}
