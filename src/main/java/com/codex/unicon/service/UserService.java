package com.codex.unicon.service;

import com.codex.unicon.dto.common.CommonErrorResponse;
import com.codex.unicon.dto.common.CommonResponse;
import com.codex.unicon.dto.request.EmailReqDto;
import com.codex.unicon.model.User_;
import com.codex.unicon.model.VerificationToken;
import com.codex.unicon.repository.TokenRepo;
import com.codex.unicon.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final EmailService emailService;
    private final UserRepo userRepo;
    private final TokenRepo tokenRepo;
    @Value("${unicon.ipaddress}")
    private String IP_ADDRESS;

    @Transactional
    public CommonResponse<?> register(EmailReqDto emailReqDto) {
        if (emailReqDto.getEmail() == null || emailReqDto.getEmail().isBlank()) {
            return new CommonResponse<>(
                new CommonErrorResponse(
                    new Date(),
                    "blank_filed",
                    "please provide all fields",
                    "some fields are blank"
                )
            );
        }
        if (userRepo.findByEmail(emailReqDto.getEmail()).isPresent()) {
            return new CommonResponse<>(
                new CommonErrorResponse(
                    new Date(),
                    "user_already_exists",
                    "user already exists",
                    "user already exists"
                )
            );
        }
        Long enrollmentNo = Long.parseLong(emailReqDto.getEmail().split("@")[0]);
        String token = UUID.randomUUID().toString();
        tokenRepo.save(
            VerificationToken.builder()
                .token(token)
                .userEmail(emailReqDto.getEmail())
                .build()
        );
        String setPasswordUrl = "http://" + IP_ADDRESS + ":8080/set-password?token=" + token;
        sendEmail(emailReqDto.getEmail(), setPasswordUrl);
        return new CommonResponse<>(
            "Email is sent please verify"
        );
    }

    private void sendEmail(String to, String setPasswordUrl) {
        emailService.sendEmail(to, "Set Your Password", "Please click on the link below to set your password:\n\n" + setPasswordUrl);
    }

    @Transactional
    public CommonResponse<?> setPassword(String token, String password) {
        VerificationToken verificationToken = tokenRepo.findByToken(token);
        if (verificationToken == null || password == null) {
            return new CommonResponse<>(
                new CommonErrorResponse(
                    new Date(),
                    "invalid_request",
                    "invalid_request",
                    "invalid_request"
                )
            );
        }
        String email = verificationToken.getUserEmail();
        if (email == null) {
            return new CommonResponse<>(
                new CommonErrorResponse(
                    new Date(),
                    "invalid_verification_token",
                    "invalid_verification_token",
                    "invalid_verification_token"
                )
            );
        }
        userRepo.save(
            User_.builder()
                .email(email)
                .password(password)
                .build()
        );
        tokenRepo.delete(verificationToken);
        return new CommonResponse<>(
            "You can login now"
        );
    }
}
