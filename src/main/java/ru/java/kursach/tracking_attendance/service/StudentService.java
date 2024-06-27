package ru.java.kursach.tracking_attendance.service;

import org.springframework.data.domain.Page;
import ru.java.kursach.tracking_attendance.model.Student;

import java.util.Optional;

public interface StudentService {
    Page<Student> findAllStudents(int page, boolean sortByLastName);

    Optional<Student> findStudentById(Long id);

    Student createStudent(Student student);

    Optional<Student> updateStudent(Long id, Student updatedStudent);

    void deleteStudent(Long id);

    Optional<Student> assignStudentToGroup(Long studentId, Long groupId);

    void removeStudentFromGroup(Long studentId, Long groupId);
}
