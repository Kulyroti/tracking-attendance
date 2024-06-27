package ru.java.kursach.tracking_attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.java.kursach.tracking_attendance.model.UserRole;


public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}
