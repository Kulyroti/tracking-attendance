package ru.java.kursach.tracking_attendance.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.java.kursach.tracking_attendance.model.Student;
import ru.java.kursach.tracking_attendance.repository.StudentRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    @Override
    public Page<Student> findAllStudents(int page, boolean sortByLastName) {
        int size = 10;
        return studentRepository.findAll(PageRequest.of(page, size, Sort.by("lastName")));
    }

    @Override
    public Optional<Student> findStudentById(Long id) {
        return studentRepository.findById(id);
    }

    @Transactional
    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    @Override
    public Optional<Student> updateStudent(Long id, Student updatedStudent) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setFirstName(updatedStudent.getFirstName());
                    student.setMiddleName(updatedStudent.getMiddleName());
                    student.setLastName(updatedStudent.getLastName());
                    student.setEmail(updatedStudent.getEmail());
                    student.setGroup(updatedStudent.getGroup());
                    return studentRepository.save(student);
                });
    }

    @Transactional
    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Optional<Student> assignStudentToGroup(Long studentId, Long groupId) {
        studentRepository.assignStudentToGroup(studentId, groupId);
        return studentRepository.findById(studentId);
    }

    @Transactional
    @Override
    public void removeStudentFromGroup(Long studentId, Long groupId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Студент не найден"));

        if (student.getGroup() != null && student.getGroup().getId().equals(groupId)) {
            student.setGroup(null);
            studentRepository.save(student);
        } else {
            throw new IllegalArgumentException("Студент не принадлежит к указанной группе");
        }
    }
}