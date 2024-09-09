package com.example.chess.Repository;

import com.example.chess.model.School;
import com.example.chess.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository  extends JpaRepository<Student, Long> {
    List<Student> findBySchool(School school);
    Optional<Student> findByStudentId(Long studentId);

}
