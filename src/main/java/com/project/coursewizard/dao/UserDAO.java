package com.project.coursewizard.dao;

import com.project.coursewizard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, String> {
}
