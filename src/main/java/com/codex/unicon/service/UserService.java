package com.codex.unicon.service;

import com.codex.unicon.dto.common.CommonErrorResponse;
import com.codex.unicon.dto.common.CommonResponse;
import com.codex.unicon.dto.request.EmailReqDto;
import com.codex.unicon.dto.request.LoginRequestDto;
import com.codex.unicon.dto.response.BasicResponseDto;
import com.codex.unicon.dto.response.TokenResponseDto;
import com.codex.unicon.model.Role;
import com.codex.unicon.model.User_;
import com.codex.unicon.model.VerificationToken;
import com.codex.unicon.repository.BasicStudentDetailsRepo;
import com.codex.unicon.repository.TokenRepo;
import com.codex.unicon.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final EmailService emailService;
    private final UserRepo userRepo;
    private final TokenRepo tokenRepo;
    private final BasicStudentDetailsRepo basicStudentDetailsRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
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
        BigInteger enrollmentNo = new BigInteger(emailReqDto.getEmail().split("@")[0]);
        if (basicStudentDetailsRepo.findByEnrollmentNo(enrollmentNo).isEmpty()) {
            return new CommonResponse<>(
                new CommonErrorResponse(
                    new Date(),
                    "user_details_not_found",
                    "user_details_not_found",
                    "user_details_not_found"
                )
            );
        }
        Optional<VerificationToken> verificationToken = tokenRepo.findByUserEmail(emailReqDto.getEmail());
        verificationToken.ifPresent(tokenRepo::delete);
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
            BasicResponseDto.builder()
                .msg("Email send successfully")
                .build()
        );
    }

    private void sendEmail(String to, String setPasswordUrl) {
        emailService.sendEmail(to, "Set Your Password", "Please click on the link below to set your password:\n\n" + setPasswordUrl);
    }

    @Transactional
    public CommonResponse<?> setPassword(String token, String password) {
        VerificationToken verificationToken = tokenRepo.findByToken(token);
        if (verificationToken == null || password == null || password.length() < 8) {
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
        BigInteger enrollmentNo = new BigInteger(email.split("@")[0]);
        userRepo.save(
            User_.builder()
                .email(email)
                .name(basicStudentDetailsRepo.findByEnrollmentNo(enrollmentNo).get().getStudentName())
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build()
        );
        tokenRepo.delete(verificationToken);
        return new CommonResponse<>(
            "You can login now"
        );
    }

    @Transactional
    public CommonResponse<TokenResponseDto> login(LoginRequestDto request) {
        if (request.getEmail() == null || request.getEmail().isBlank()
            || request.getPassword() == null || request.getPassword().isBlank()
        ) {
            return new CommonResponse<>(
                new CommonErrorResponse(
                    new Date(),
                    "blank_filed",
                    "please provide all fields",
                    "some fields are blank"
                )
            );
        }
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );
        } catch (AuthenticationException e) {
            return new CommonResponse<>(
                new CommonErrorResponse(
                    new Date(),
                    "login_failed",
                    "please enter valid credentials",
                    "invalid email or password"
                )
            );
        }
        var user = userRepo.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            return new CommonResponse<>(
                new CommonErrorResponse(
                    new Date(),
                    "login_failed",
                    "please enter valid credentials",
                    "invalid email or password"
                )
            );
        }
        var jwtToken = jwtService.generateToken(user);
        return new CommonResponse<>(
            TokenResponseDto.builder()
                .token(jwtToken)
                .build()
        );
    }

    @Transactional
    public CommonResponse<?> getUserName(String email) {
        Optional<User_> user = userRepo.findByEmail(email);
        if (user.isEmpty()) {
            return new CommonResponse<>(
                new CommonErrorResponse(
                    new Date(),
                    "invalid_verification_token",
                    "invalid_verification_token",
                    "invalid_verification_token"
                )
            );
        }
        return new CommonResponse<>(
            BasicResponseDto.builder()
                .msg(user.get().getName())
                .build()
        );
    }
}
