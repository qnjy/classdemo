package com.example.repositoty;

import com.example.entity.Grade;
import com.example.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade,Integer> {
}
