# **🏧 小學出缺勤管理系統**

本專案是一個基於 **Spring Boot + JPA + MySQL** 的小學出缺勤管理系統，提供 API 查詢每個班級的全勤學生名單。

---

## **💁️ 檔案架構**
```
💀 school-attendance/
🔗 src/
👉 main/
👉 java/com/example/school/
👉 config/         # 配置類，例如 DataLoader (初始化測試數據)
👉 controller/     # 控制器類 (API 入口)
👉 entity/         # JPA 實體類
👉 repository/     # JPA Repository
👉 service/        # 服務層邏輯
👉 DemoApplication.java  # 主應用程式
🔗 resources/
👉 application.properties   # MySQL 連線配置
👉 schema.sql               # 資料庫表結構
🔗 README.md                         # 本文件
🔗 pom.xml                            # Maven 設定檔
```

---

## **📚 Table Schema 設計**
本系統包含 3 張主要的資料表：
1. **`classes`** (班級表)
2. **`students`** (學生表)
3. **`attendance`** (出缺勤表)

### **📌 資料表關聯**
- **一個 `class` (班級) 可對應多個 `student` (學生)** (`OneToMany`)
- **一個 `student` (學生) 可對應多條 `attendance` (出缺勤記錄)** (`OneToMany`)
- **`attendance` 記錄 `student_id`，標記學生當日是否 `PRESENT` (出席) 或 `ABSENT` (缺席)**

### **📌 Table Schema**
```sql
CREATE TABLE classes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    class_id INT NOT NULL,
    FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE
);

CREATE TABLE attendance (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    date DATE NOT NULL,
    status ENUM('PRESENT', 'ABSENT') NOT NULL,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);
```

---

## **🖊️ SQL 指令: 查詢每月每班全勤學生名單**
```sql
SELECT s.id, s.name, s.class_id, c.name AS class_name
FROM students s
JOIN classes c ON s.class_id = c.id
WHERE NOT EXISTS (
    SELECT 1 FROM attendance a
    WHERE a.student_id = s.id
    AND a.date BETWEEN '2025-02-01' AND '2025-02-28'
    AND a.status = 'ABSENT'
);
```

---

## **🚀 如何測試**
### **1. 環境設置**
- 安裝 **MySQL**，並建立 `school` 資料庫
- `application.properties` 設定 MySQL 連線

### **2. 啟動專案**
```bash
mvn spring-boot:run
```

### **3. 使用 Postman 測試 API**
#### **📌 測試 API: 查詢 `2025年2月` `1A班` 的全勤學生**
- **請求 (GET)**
  ```
  GET http://localhost:8081/attendance/full-attendance/1/2025/2
  ```
- **回應 (JSON)**
  ```json
  [
      {"id":1,"name":"王小明","classEntity":{"id":1,"name":"1A"}},
      {"id":3,"name":"張大山","classEntity":{"id":1,"name":"1A"}}
  ]
  ```

---

## **🛠️ 遇到的難點 & 解決方案**

### **1. `java.lang.ArrayIndexOutOfBoundsException`**
**🔄 解決**: 修正 SQL 查詢，增加 `class_name` 字段

### **2. `TransactionRequiredException`**
**🔄 解決**: 為編輯資料加上 `@Transactional`

### **3. API URL 格式疑問**
**🔄 解決**: `/1/` 作為 `classId`，選擇是否需要查詢所有班級

---

## **🎉 結論**
✔️ **成功完成 Spring Boot + JPA + MySQL 的小學出缺勤管理系統**
✔️ **修正 SQL 問題，確保 API 可正常輸出**
✔️ **完整測試，正确取得每月每班全勤學生名單**