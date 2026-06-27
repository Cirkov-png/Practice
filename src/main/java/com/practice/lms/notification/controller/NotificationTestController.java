package com.practice.lms.notification.controller;

import com.practice.lms.notification.service.CourseReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test/notifications")
@RequiredArgsConstructor
@Profile("dev")
public class NotificationTestController {

    private final CourseReminderService reminderService;

    @PostMapping("/trigger")
    public String triggerReminders() {
        reminderService.sendRemindersForCoursesStartingTomorrow();
        return "Reminder job triggered";
    }
}
