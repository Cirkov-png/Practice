package com.practice.lms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lesson")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 2000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}