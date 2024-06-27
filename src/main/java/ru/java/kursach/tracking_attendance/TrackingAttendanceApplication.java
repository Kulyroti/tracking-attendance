package ru.java.kursach.tracking_attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableSpringDataWebSupport
@SpringBootApplication
public class TrackingAttendanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrackingAttendanceApplication.class, args);
	}

}
