package ru.java.kursach.tracking_attendance.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.java.kursach.tracking_attendance.model.Student;

@Repository
public interface StudentRepository extends PagingAndSortingRepository<Student, Long>, JpaRepository<Student, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Student s SET s.group.id = :groupId WHERE s.id = :studentId")
    void assignStudentToGroup(Long studentId, Long groupId);
}
