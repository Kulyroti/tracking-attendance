package ru.java.kursach.tracking_attendance.service;

import org.springframework.data.domain.Page;
import ru.java.kursach.tracking_attendance.model.Teacher;

import java.util.Optional;

public interface TeacherService {
    Page<Teacher> findAllTeachers(int page, boolean sortByLastName);

    Optional<Teacher> findTeacherById(Long id);

    Teacher createTeacher(Teacher teacher);

    Optional<Teacher> updateTeacher(Long id, Teacher updatedTeacher);

    void deleteTeacher(Long id);
}
