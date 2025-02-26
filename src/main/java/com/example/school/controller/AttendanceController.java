package com.example.school.controller;

import com.example.school.entity.Student;
import com.example.school.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/full-attendance/{classId}/{year}/{month}")
    public List<Student> getFullAttendanceStudents(
            @PathVariable Long classId,
            @PathVariable int year,
            @PathVariable int month
    ) {
        return attendanceService.getFullAttendanceStudents(classId, year, month);
    }
}
