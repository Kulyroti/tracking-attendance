package ru.java.kursach.tracking_attendance.service;

import org.springframework.data.domain.Page;
import ru.java.kursach.tracking_attendance.model.Lesson;

import java.util.Optional;

public interface LessonService {

    Page<Lesson> findAllLessons(int page, boolean sortByDate);

    Optional<Lesson> findLessonById(Long id);

    Lesson createLesson(Lesson lesson);

    Optional<Lesson> updateLesson(Long id, Lesson updatedLesson);

    void deleteLesson(Long id);
}
