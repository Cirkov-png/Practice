package com.practice.lms.notification.service;

import java.time.LocalDateTime;

public interface EmailNotificationService {

    void sendCourseReminder(String recipientEmail, String studentName, String courseTitle, LocalDateTime startDate);
}
