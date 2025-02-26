package com.example.school.service;

import com.example.school.entity.ClassEntity;
import com.example.school.entity.Student;
import com.example.school.repository.AttendanceRepository;
import com.example.school.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getFullAttendanceStudents(Long classId, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Object[]> rawResults = attendanceRepository.findPerfectAttendanceStudentsRaw(classId, startDate, endDate);
        List<Student> students = new ArrayList<>();

        for (Object[] row : rawResults) {
            Student student = new Student();
            student.setId(((Number) row[0]).longValue());
            student.setName((String) row[1]);

            ClassEntity classEntity = new ClassEntity();
            classEntity.setId(((Number) row[2]).longValue());

            // ✅ 確保 class_name 存在，避免 IndexOutOfBoundsException
            if (row.length > 3) {
                classEntity.setName((String) row[3]);
            } else {
                classEntity.setName("Unknown Class"); // 預設名稱，防止錯誤
            }

            student.setClassEntity(classEntity);
            students.add(student);
        }

        return students;
    }
}