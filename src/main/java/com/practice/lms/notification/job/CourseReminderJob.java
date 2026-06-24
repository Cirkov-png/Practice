package com.practice.lms.notification.job;

import com.practice.lms.notification.service.CourseReminderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CourseReminderJob {

    private final CourseReminderService reminderService;

    @Scheduled(cron = "${course.reminder.cron}")
    public void sendDailyCourseReminders() {
        log.info("Starting daily course reminder job");
        reminderService.sendRemindersForCoursesStartingTomorrow();
        log.info("Daily course reminder job completed");
    }
}
