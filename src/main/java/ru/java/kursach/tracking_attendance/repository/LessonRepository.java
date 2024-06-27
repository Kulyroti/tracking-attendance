package ru.java.kursach.tracking_attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.java.kursach.tracking_attendance.model.Lesson;


@Repository
public interface LessonRepository extends PagingAndSortingRepository<Lesson, Long>, JpaRepository<Lesson, Long> {

}
