package ru.java.kursach.tracking_attendance.service;

import ru.java.kursach.tracking_attendance.model.LessonType;

import java.util.List;
import java.util.Optional;

public interface LessonTypeService {

    List<LessonType> findAllLessonTypes();

    Optional<LessonType> findLessonTypeById(Long id);

    LessonType createLessonType(LessonType lessonType);

    Optional<LessonType> updateLessonType(Long id, LessonType updatedLessonType);

    void deleteLessonType(Long id);
}
