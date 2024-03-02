package com.codex.unicon.repository;

import com.codex.unicon.model.User_;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User_,Integer> {
    Optional<User_> findByEmail(String email);
}
