package ru.java.kursach.tracking_attendance.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.kursach.tracking_attendance.model.*;
import ru.java.kursach.tracking_attendance.repository.*;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService{
    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;
    private final LessonTypeRepository lessonTypeRepository;

    @Override
    public Page<Lesson> findAllLessons(int page, boolean sortByDate) {
        int size = 10;
        return lessonRepository.findAll(PageRequest.of(page, size, Sort.by("date")));
    }

    @Override
    public Optional<Lesson> findLessonById(Long id) {
        return lessonRepository.findById(id);
    }

    @Transactional
    @Override
    public Lesson createLesson(Lesson lesson) {

        Teacher teacher = teacherRepository.findById(lesson.getTeacher().getId())
                .orElseThrow(() -> new EntityNotFoundException("Преподаватель не найден"));
        Group group = groupRepository.findById(lesson.getGroup().getId())
                .orElseThrow(() -> new EntityNotFoundException("Группа не найдена"));
        LessonType lessonType = lessonTypeRepository.findById(lesson.getLessonType().getId())
                .orElseThrow(() -> new EntityNotFoundException("Занятие не найдено"));

        lesson.setTeacher(teacher);
        lesson.setGroup(group);
        lesson.setLessonType(lessonType);

        return lessonRepository.save(lesson);
    }

    @Transactional
    @Override
    public Optional<Lesson> updateLesson(Long id, Lesson updatedLesson) {
        return lessonRepository.findById(id)
                .map(lesson -> {
                    lesson.setDiscipline(updatedLesson.getDiscipline());
                    lesson.setTeacher(updatedLesson.getTeacher());
                    lesson.setGroup(updatedLesson.getGroup());
                    lesson.setDate(updatedLesson.getDate());
                    lesson.setClassroom(updatedLesson.getClassroom());
                    lesson.setTime(updatedLesson.getTime());
                    return lessonRepository.save(lesson);
                });
    }

    @Transactional
    @Override
    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }
}
