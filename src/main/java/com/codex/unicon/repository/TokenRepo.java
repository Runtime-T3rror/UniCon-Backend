package com.codex.unicon.repository;

import com.codex.unicon.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepo extends JpaRepository<VerificationToken,Integer> {
    VerificationToken findByToken(String token);
}
