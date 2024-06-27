package ru.java.kursach.tracking_attendance.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "students")
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_generator")
    @SequenceGenerator(name = "student_generator", sequenceName = "student_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

}
