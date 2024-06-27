package ru.java.kursach.tracking_attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.java.kursach.tracking_attendance.model.LessonType;

public interface LessonTypeRepository extends JpaRepository<LessonType, Long> {

}
