package com.project.coursewizard.dao;

import com.project.coursewizard.entity.Student;
import com.project.coursewizard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDAO extends JpaRepository<User, String> {
    List<User> findByUserName(String userName);
}
