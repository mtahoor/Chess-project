package com.example.chess.Repository;

import com.example.chess.model.User;
import com.example.chess.model.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolRepository extends JpaRepository<School, Long> {
    School findByName(String name);
//    List<School> findAllBySchoolId();
    School findBySchoolId(int id);
    School findByUser(User user);
}