package com.practice.lms.enrollment.model;

import com.practice.lms.common.model.Money;
import com.practice.lms.course.model.Course;
import com.practice.lms.student.model.Student;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = Enrollment.TABLE_NAME)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {
    public static final String TABLE_NAME = "enrollments";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "coins_paid", nullable = false))
    private Money coinsPaid;
}
