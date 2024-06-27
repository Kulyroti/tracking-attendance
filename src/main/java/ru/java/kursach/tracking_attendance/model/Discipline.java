package ru.java.kursach.tracking_attendance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "disciplines")
@Getter
@Setter
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discipline_generator")
    @SequenceGenerator(name = "discipline_generator", sequenceName = "discipline_seq", initialValue = 1, allocationSize = 1)
    private Long id;

    private String name;

}
