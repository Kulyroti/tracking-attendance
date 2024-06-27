package ru.java.kursach.tracking_attendance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.java.kursach.tracking_attendance.model.Attendance;
import ru.java.kursach.tracking_attendance.service.AttendanceService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/attendances")
public class AttendanceController {
    private final AttendanceService attendanceService;

    @GetMapping
    public Page<Attendance> getAllAttendances(@RequestParam int page) {
        return attendanceService.findAllAttendances(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Attendance> getAttendanceById(@PathVariable Long id) {
        return attendanceService.findAttendanceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Attendance createAttendance(@RequestBody Attendance attendance) {
        return attendanceService.createAttendance(attendance);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable Long id, @RequestBody Attendance attendance) {
        return attendanceService.updateAttendance(id, attendance)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/mark/{id}")
    public ResponseEntity<Attendance> updatePresentAttendance(@PathVariable Long id, @RequestBody Boolean present) {
        Optional<Attendance> updatedAttendance = Optional.ofNullable(attendanceService.updatePresentAttendance(id, present));
        return updatedAttendance.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    public List<Attendance> getAttendanceForStudent(@PathVariable Long studentId) {
        return attendanceService.getAttendanceForStudent(studentId);
    }

    @GetMapping("/group/{groupId}")
    public List<Attendance> getAttendanceForGroup(@PathVariable Long groupId) {
        return attendanceService.getAttendanceForGroup(groupId);
    }

    @GetMapping("/lesson/{lessonId}")
    public List<Attendance> getAttendanceForLesson(@PathVariable Long lessonId) {
        return attendanceService.getAttendanceForLesson(lessonId);
    }

    @GetMapping("/period")
    public List<Attendance> getAttendanceForPeriod(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return attendanceService.getAttendanceForPeriod(startDate, endDate);
    }
}
