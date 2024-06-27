package ru.java.kursach.tracking_attendance.service;

import org.springframework.data.domain.Page;
import ru.java.kursach.tracking_attendance.model.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceService {

    Page<Attendance> findAllAttendances(int page);

    Optional<Attendance> findAttendanceById(Long id);

    Attendance createAttendance(Attendance attendance);

    Optional<Attendance> updateAttendance(Long id, Attendance updatedAttendance);

    void deleteAttendance(Long id);

    Attendance updatePresentAttendance(Long attendanceId, Boolean present);

    List<Attendance> getAttendanceForStudent(Long studentId);

    List<Attendance> getAttendanceForGroup(Long groupId);

    List<Attendance> getAttendanceForLesson(Long lessonId);

    List<Attendance> getAttendanceForPeriod(LocalDate startDate, LocalDate endDate);
}
