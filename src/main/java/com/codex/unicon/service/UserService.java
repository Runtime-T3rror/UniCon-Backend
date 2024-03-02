package com.codex.unicon.service;

import com.codex.unicon.dto.common.CommonErrorResponse;
import com.codex.unicon.dto.common.CommonResponse;
import com.codex.unicon.dto.request.EmailReqDto;
import com.codex.unicon.model.VerificationToken;
import com.codex.unicon.repository.TokenRepo;
import com.codex.unicon.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final EmailService emailService;
    private final UserRepo userRepo;
    private final TokenRepo tokenRepo;
    public CommonResponse<?> register(EmailReqDto emailReqDto) {
        if(emailReqDto.getEmail() == null || emailReqDto.getEmail().isBlank()) {
            return new CommonResponse<>(
                new CommonErrorResponse(
                    new Date(),
                    "blank_filed",
                    "please provide all fields",
                    "some fields are blank"
                )
            );
        }
        if(userRepo.findByEmail(emailReqDto.getEmail())!=null) {
            return new CommonResponse<>(
                new CommonErrorResponse(
                    new Date(),
                    "user_already_exists",
                    "user already exists",
                    "user already exists"
                )
            );
        }
        String token = UUID.randomUUID().toString();
        tokenRepo.save(
            VerificationToken.builder()
                .token(token)
                .userEmail(emailReqDto.getEmail())
                .build()
        );
        String setPasswordUrl = "http://192.168.127.46:8080/set-password?token=" + token;
        sendEmail(emailReqDto.getEmail(), setPasswordUrl);
        return new CommonResponse<>(
            "Email is sent please verify"
        );
    }

    private void sendEmail(String to, String setPasswordUrl) {
        emailService.sendEmail(to,"Set Your Password","Please click on the link below to set your password:\n\n" + setPasswordUrl);
    }
}
