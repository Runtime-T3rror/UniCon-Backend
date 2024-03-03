package com.codex.unicon.repository;

import com.codex.unicon.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepo extends JpaRepository<VerificationToken, Integer> {
    VerificationToken findByToken(String token);

    Optional<VerificationToken> findByUserEmail(String userEmail);
}
