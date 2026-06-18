package com.practice.lms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "course_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String difficulty;
    private Integer duration;
    private Boolean isPublic;


    @OneToOne
    @JoinColumn(name = "course_id", unique = true)
    private Course course;
}