package ru.java.kursach.tracking_attendance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lessonsTypes")
@Getter
@Setter
public class LessonType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lesson_type_generator")
    @SequenceGenerator(name = "lesson_type_generator", sequenceName = "lesson_type_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    private String name;
}
