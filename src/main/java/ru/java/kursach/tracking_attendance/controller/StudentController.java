package ru.java.kursach.tracking_attendance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.java.kursach.tracking_attendance.model.Student;
import ru.java.kursach.tracking_attendance.service.StudentService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public Page<Student> getAllStudents(@RequestParam int page,
                                        @RequestParam boolean sortByLastName) {
        return studentService.findAllStudents(page, sortByLastName);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentService.findStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentService.updateStudent(id, student)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{studentId}/groups/{groupId}")
    public ResponseEntity<Student> assignStudentToGroup(@PathVariable Long studentId, @PathVariable Long groupId) {
        return studentService.assignStudentToGroup(studentId, groupId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{studentId}/groups/{groupId}")
    public ResponseEntity<Void> removeStudentFromGroup(@PathVariable Long studentId, @PathVariable Long groupId) {
        studentService.removeStudentFromGroup(studentId, groupId);
        return ResponseEntity.noContent().build();
    }

}
