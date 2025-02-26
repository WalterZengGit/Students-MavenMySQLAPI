package com.example.school.repository;

import com.example.school.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @Query(value = """
    SELECT s.id, s.name, s.class_id, c.name as class_name
    FROM students s
    JOIN classes c ON s.class_id = c.id
    WHERE s.class_id = :classId
    AND NOT EXISTS (
        SELECT 1 FROM attendance a 
        WHERE a.student_id = s.id 
        AND a.date BETWEEN :startDate AND :endDate 
        AND a.status = 'ABSENT'
    )
""", nativeQuery = true)
    List<Object[]> findPerfectAttendanceStudentsRaw(@Param("classId") Long classId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}


