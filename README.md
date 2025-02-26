# School Attendance System

## 1. Project Structure

```
project-root/
│── src/
│   ├── main/
│   │   ├── java/com/example/school/
│   │   │   ├── config/
│   │   │   │   ├── DataLoader.java
│   │   │   ├── controller/
│   │   │   │   ├── AttendanceController.java
│   │   │   ├── entity/
│   │   │   │   ├── Attendance.java
│   │   │   │   ├── ClassEntity.java
│   │   │   │   ├── Student.java
│   │   │   ├── repository/
│   │   │   │   ├── AttendanceRepository.java
│   │   │   │   ├── ClassRepository.java
│   │   │   │   ├── StudentRepository.java
│   │   │   ├── service/
│   │   │   │   ├── AttendanceService.java
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   ├── schema.sql
│── pom.xml
│── README.md
```

## 2. Database Schema and Relationships

### Table: `classes`
- `id` (INT, Primary Key, AUTO_INCREMENT)
- `name` (VARCHAR(50), NOT NULL)

### Table: `students`
- `id` (INT, Primary Key, AUTO_INCREMENT)
- `name` (VARCHAR(50), NOT NULL)
- `class_id` (INT, FOREIGN KEY REFERENCES `classes(id)`, ON DELETE CASCADE)

### Table: `attendance`
- `id` (INT, Primary Key, AUTO_INCREMENT)
- `student_id` (INT, FOREIGN KEY REFERENCES `students(id)`, ON DELETE CASCADE)
- `date` (DATE, NOT NULL)
- `status` (ENUM('PRESENT', 'ABSENT'), NOT NULL)

### SQL Query: Retrieve Fully Attended Students Per Class Per Month
```sql
SELECT s.id, s.name, s.class_id
FROM students s
WHERE s.class_id = ?
AND NOT EXISTS (
    SELECT 1 FROM attendance a
    WHERE a.student_id = s.id
    AND a.date BETWEEN '2025-02-01' AND '2025-02-28'
    AND a.status = 'ABSENT'
);
```

## 3. Testing Methodology and Results

### Steps to Test
1. Start the Spring Boot application.
2. Use Postman or a browser to test the API:
  - GET `http://localhost:8081/attendance/full-attendance/{classId}/{year}/{month}`
3. Check the response to verify if the list of fully attended students is returned correctly.

### Test Results
- **Successful Case**: If all students attended every class in the specified month, the API returns a list of fully attended students.
- **Edge Case**: If no students meet the criteria, an empty array is returned.

## 4. Challenges and Solutions

### (1) Foreign Key Constraints Preventing Data Deletion
**Issue:** Using `TRUNCATE TABLE` directly fails due to foreign key constraints.
**Solution:** In the `DataLoader.java` `clearDatabase()` method, temporarily disable foreign key checks before truncating tables:
```java
entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
entityManager.createNativeQuery("TRUNCATE TABLE attendance").executeUpdate();
entityManager.createNativeQuery("TRUNCATE TABLE students").executeUpdate();
entityManager.createNativeQuery("TRUNCATE TABLE classes").executeUpdate();
entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
```

### (2) `ArrayIndexOutOfBoundsException` Issue
**Issue:** `AttendanceService.getFullAttendanceStudents()` tries to access index 3 of the `Object[]` array when the SQL query does not return enough columns.
**Solution:** Ensure that the SQL query returns the correct number of fields and verify field mappings before accessing array elements.

### (3) `deleteAll()` Not Immediately Affecting Database
**Issue:** `deleteAll()` from Spring Data JPA was not being applied immediately.
**Solution:** Use `@Transactional` annotation to ensure database operations execute successfully.
