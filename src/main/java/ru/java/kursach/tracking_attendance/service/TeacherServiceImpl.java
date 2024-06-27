package ru.java.kursach.tracking_attendance.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.java.kursach.tracking_attendance.model.Teacher;
import ru.java.kursach.tracking_attendance.repository.TeacherRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService{
    private final TeacherRepository teacherRepository;

    @Override
    public Page<Teacher> findAllTeachers(int page, boolean sortByLastName) {
        int size = 10;
        return teacherRepository.findAll(PageRequest.of(page, size, Sort.by("lastName")));
    }

    @Override
    public Optional<Teacher> findTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    @Transactional
    @Override
    public Teacher createTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Transactional
    @Override
    public Optional<Teacher> updateTeacher(Long id, Teacher updatedTeacher) {
        return teacherRepository.findById(id)
                .map(teacher -> {
                    teacher.setFirstName(updatedTeacher.getFirstName());
                    teacher.setMiddleName(updatedTeacher.getMiddleName());
                    teacher.setLastName(updatedTeacher.getLastName());
                    teacher.setEmail(updatedTeacher.getEmail());
                    return teacherRepository.save(teacher);
                });
    }

    @Transactional
    @Override
    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }


}
