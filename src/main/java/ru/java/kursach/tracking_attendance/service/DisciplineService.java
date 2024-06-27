package ru.java.kursach.tracking_attendance.service;

import org.springframework.data.domain.Page;
import ru.java.kursach.tracking_attendance.model.Discipline;

import java.util.Optional;

public interface DisciplineService {

    Page<Discipline> findAllDisciplines(int page, boolean sortByName);

    Optional<Discipline> findDisciplineById(Long id);

    Discipline createDiscipline(Discipline discipline);

    Optional<Discipline> updateDiscipline(Long id, Discipline updatedDiscipline);

    void deleteDiscipline(Long id);
}
