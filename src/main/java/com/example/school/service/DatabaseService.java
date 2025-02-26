package com.example.school.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void clearDatabase() {
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE attendance").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE students").executeUpdate();
        entityManager.createNativeQuery("TRUNCATE TABLE classes").executeUpdate();
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        System.out.println("✅ 資料庫已清空！");
    }
}