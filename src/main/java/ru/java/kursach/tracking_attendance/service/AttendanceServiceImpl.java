package ru.java.kursach.tracking_attendance.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.kursach.tracking_attendance.model.*;
import ru.java.kursach.tracking_attendance.repository.*;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService{
    private final AttendanceRepository attendanceRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;

    @Override
    public Page<Attendance> findAllAttendances(int page) {
        int size = 10;
        return attendanceRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Optional<Attendance> findAttendanceById(Long id) {
        return attendanceRepository.findById(id);
    }

    @Transactional
    @Override
    public Attendance createAttendance(Attendance attendance) {

        Lesson lesson = lessonRepository.findById(attendance.getLesson().getId())
                .orElseThrow(() -> new EntityNotFoundException("Занятие не найдено"));

        Student student = studentRepository.findById(attendance.getStudent().getId())
                .orElseThrow(() -> new EntityNotFoundException("Студент не найден"));

        attendance.setLesson(lesson);
        attendance.setStudent(student);

        return attendanceRepository.save(attendance);
    }

    @Transactional
    @Override
    public Optional<Attendance> updateAttendance(Long id, Attendance updatedAttendance) {
        return attendanceRepository.findById(id)
                .map(attendance -> {
                    attendance.setLesson(updatedAttendance.getLesson());
                    attendance.setStudent(updatedAttendance.getStudent());
                    return attendanceRepository.save(attendance);
                });
    }

    @Transactional
    @Override
    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Attendance updatePresentAttendance(Long attendanceId, Boolean present) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new EntityNotFoundException("Посещение не найдено"));

        attendance.setPresent(present);

        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceForStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    public List<Attendance> getAttendanceForGroup(Long groupId) {
        return attendanceRepository.findByLesson_GroupId(groupId);
    }

    public List<Attendance> getAttendanceForLesson(Long lessonId) {
        return attendanceRepository.findByLessonId(lessonId);
    }

    public List<Attendance> getAttendanceForPeriod(LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByLesson_DateBetween(startDate, endDate);
    }
}
