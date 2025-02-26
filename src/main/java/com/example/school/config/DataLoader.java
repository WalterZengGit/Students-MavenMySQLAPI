package com.example.school.config;

import com.example.school.entity.Attendance;
import com.example.school.entity.ClassEntity;
import com.example.school.entity.Student;
import com.example.school.repository.AttendanceRepository;
import com.example.school.repository.ClassRepository;
import com.example.school.repository.StudentRepository;
import com.example.school.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private DatabaseService databaseService; // ✅ 使用 Service 層來清空資料庫

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 清除舊有資料...");
        databaseService.clearDatabase(); // ✅ 讓 `DatabaseService` 處理 TRUNCATE

        System.out.println("✅ 已清空資料庫，開始插入測試數據...");

        // **1. 插入班級**
        ClassEntity class1A = new ClassEntity();
        class1A.setName("1A");

        ClassEntity class1B = new ClassEntity();
        class1B.setName("1B");

        classRepository.saveAll(List.of(class1A, class1B));

        System.out.println("✅ 已插入 `classes` 表");

        // **2. 插入學生**
        Student student1 = new Student("王小明", class1A);
        Student student2 = new Student("李小華", class1A);
        Student student3 = new Student("張大山", class1A);
        Student student4 = new Student("陳美麗", class1B);
        Student student5 = new Student("林志遠", class1B);

        studentRepository.saveAll(List.of(student1, student2, student3, student4, student5));

        System.out.println("✅ 已插入 `students` 表");

        // **3. 插入出缺勤紀錄**
        insertAttendance(student1, true);  // 王小明 (全勤)
        insertAttendance(student2, false); // 李小華 (缺席 2/3)
        insertAttendance(student3, true);  // 張大山 (全勤)
        insertAttendance(student4, false); // 陳美麗 (缺席 2/2)
        insertAttendance(student5, true);  // 林志遠 (全勤)

        System.out.println("✅ 已插入 `attendance` 表");
        System.out.println("🚀 測試資料已成功載入！");
    }

    // **插入出缺勤紀錄**
    private void insertAttendance(Student student, boolean fullAttendance) {
        for (int day = 1; day <= 5; day++) {
            Attendance attendance = new Attendance();
            attendance.setStudent(student);
            attendance.setDate(LocalDate.of(2025, 2, day));

            if (fullAttendance) {
                attendance.setStatus(Attendance.AttendanceStatus.PRESENT);
            } else {
                if ((student.getName().equals("李小華") && day == 3) ||
                        (student.getName().equals("陳美麗") && day == 2)) {
                    attendance.setStatus(Attendance.AttendanceStatus.ABSENT);
                } else {
                    attendance.setStatus(Attendance.AttendanceStatus.PRESENT);
                }
            }
            attendanceRepository.save(attendance);
        }
    }
}