package com.codex.unicon.repository;

import com.codex.unicon.model.User_;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User_,Integer> {
    User_ findByEmail(String email);
}
