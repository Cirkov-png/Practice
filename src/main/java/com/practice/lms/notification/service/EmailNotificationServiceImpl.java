package com.practice.lms.notification.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String fromEmail;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @jakarta.annotation.PostConstruct
    public void logMailConfig() {
        log.info("Mail config — from: {}, username set: {}", fromEmail, !mailUsername.isBlank());
    }

    @Override
    @Async("emailTaskExecutor")
    public void sendCourseReminder(
            final String recipientEmail,
            final String studentName,
            final String courseTitle,
            final LocalDateTime startDate
    ) {
        try {
            final MimeMessage message = mailSender.createMimeMessage();
            final var helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(recipientEmail);
            helper.setSubject("Course reminder: %s".formatted(courseTitle));
            helper.setText("""
                    Hello, %s!

                    Your course "%s" starts tomorrow at %s.

                    See you in class!
                    LMS Team
                    """.formatted(studentName, courseTitle, startDate.format(DATE_TIME_FORMATTER)));

            mailSender.send(message);
            log.info("Course reminder email sent to: {}", recipientEmail);
        } catch (Exception ex) {
            log.error("Failed to send reminder email to: {}", recipientEmail, ex);
        }
    }
}
