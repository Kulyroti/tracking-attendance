package ru.java.kursach.tracking_attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.java.kursach.tracking_attendance.model.Attendance;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository <Attendance, Long>{
    List<Attendance> findByStudentId(Long studentId);

    List<Attendance> findByLesson_GroupId(Long groupId);

    List<Attendance> findByLessonId(Long lessonId);

    @Query("SELECT a FROM Attendance a JOIN a.lesson l WHERE l.date BETWEEN :startDate AND :endDate")
    List<Attendance> findByLesson_DateBetween(LocalDate startDate, LocalDate endDate);
}
