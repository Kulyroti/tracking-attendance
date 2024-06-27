package ru.java.kursach.tracking_attendance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.java.kursach.tracking_attendance.model.LessonType;
import ru.java.kursach.tracking_attendance.service.LessonTypeService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/lessons/types")
public class LessonTypeController {
    private final LessonTypeService lessonTypeService;

    @GetMapping
    public List<LessonType> getAllLessonTypes() {
        return lessonTypeService.findAllLessonTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonType> getLessonTypeById(@PathVariable Long id) {
        return lessonTypeService.findLessonTypeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public LessonType createLessonType(@RequestBody LessonType lessonType) {
        return lessonTypeService.createLessonType(lessonType);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonType> updateLessonType(@PathVariable Long id, @RequestBody LessonType lessonType) {
        return lessonTypeService.updateLessonType(id, lessonType)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLessonType(@PathVariable Long id) {
        lessonTypeService.deleteLessonType(id);
        return ResponseEntity.noContent().build();
    }
}
