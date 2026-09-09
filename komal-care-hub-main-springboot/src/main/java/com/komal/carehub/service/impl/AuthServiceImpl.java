package com.komal.carehub.service.impl;

import com.komal.carehub.dto.AuthRequest;
import com.komal.carehub.dto.AuthResponse;
import com.komal.carehub.dto.RegisterRequest;
import com.komal.carehub.dto.ResetPasswordRequest;
import com.komal.carehub.entity.OtpSession;
import com.komal.carehub.entity.User;
import com.komal.carehub.entity.enums.Role;
import com.komal.carehub.exception.BadRequestException;
import com.komal.carehub.exception.ResourceNotFoundException;
import com.komal.carehub.repository.OtpSessionRepository;
import com.komal.carehub.repository.UserRepository;
import com.komal.carehub.security.CustomUserDetails;
import com.komal.carehub.security.JwtUtil;
import com.komal.carehub.service.AuthService;
import com.komal.carehub.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final OtpSessionRepository otpSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Override
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("User does not exist"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Incorrect password");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        
        String jwtToken = jwtUtil.generateToken(new CustomUserDetails(user));
        return AuthResponse.builder()
                .id(user.getId())
                .token(jwtToken)
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .profilePicture(user.getProfilePicture())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new BadRequestException("Email already exists!");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER) // Default role
                .build();

        userRepository.save(user);
        String jwtToken = jwtUtil.generateToken(new CustomUserDetails(user));

        return AuthResponse.builder()
                .id(user.getId())
                .token(jwtToken)
                .email(user.getEmail())
                .name(user.getName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .profilePicture(user.getProfilePicture())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with this email not found"));
        
        String otp = String.format("%06d", new Random().nextInt(999999));
        
        OtpSession session = OtpSession.builder()
                .email(email)
                .otp(otp)
                .expiryTime(LocalDateTime.now().plusMinutes(10))
                .isUsed(false)
                .build();
                
        otpSessionRepository.save(session);
        emailService.sendOtpEmail(email, otp);
    }

    @Override
    public void verifyOtp(String email, String otp) {
        OtpSession session = otpSessionRepository.findByEmailAndOtpAndIsUsedFalse(email, otp)
                .orElseThrow(() -> new BadRequestException("Invalid OTP"));
                
        if (session.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP has expired");
        }
        
        session.setUsed(true);
        otpSessionRepository.save(session);
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}

