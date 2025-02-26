package com.example.school.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classEntity;

    // **✅ 預設無參數建構子 (JPA 需要)**
    public Student() {}

    // **✅ 新增帶參數的建構子**
    public Student(String name, ClassEntity classEntity) {
        this.name = name;
        this.classEntity = classEntity;
    }

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ClassEntity getClassEntity() { return classEntity; }
    public void setClassEntity(ClassEntity classEntity) { this.classEntity = classEntity; }
}
