package com.komal.carehub.controller;

import com.komal.carehub.dto.AuthResponse;
import com.komal.carehub.dto.PasswordUpdateDto;
import com.komal.carehub.dto.UserProfileDto;
import com.komal.carehub.entity.User;
import com.komal.carehub.exception.BadRequestException;
import com.komal.carehub.exception.ResourceNotFoundException;
import com.komal.carehub.repository.UserRepository;
import com.komal.carehub.security.CustomUserDetails;
import com.komal.carehub.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new ResourceNotFoundException("Not authenticated");
        }
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @GetMapping
    public ResponseEntity<AuthResponse> getProfile() {
        User user = getAuthenticatedUser();
        return ResponseEntity.ok(
            AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .city(user.getCity())
                .zipCode(user.getZipCode())
                .profilePicture(user.getProfilePicture())
                .role(user.getRole().name())
                .build()
        );
    }

    @PutMapping
    public ResponseEntity<AuthResponse> updateProfile(@RequestBody UserProfileDto dto) {
        User user = getAuthenticatedUser();
        
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity());
        user.setZipCode(dto.getZipCode());
        user.setProfilePicture(dto.getProfilePicture());
        
        userRepository.save(user);

        String jwtToken = jwtUtil.generateToken(new CustomUserDetails(user));
        return ResponseEntity.ok(
            AuthResponse.builder()
                .id(user.getId())
                .token(jwtToken)
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .city(user.getCity())
                .zipCode(user.getZipCode())
                .profilePicture(user.getProfilePicture())
                .role(user.getRole().name())
                .build()
        );
    }

    @PutMapping("/password")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateDto dto) {
        User user = getAuthenticatedUser();
        
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Incorrect old password");
        }
        
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        
        return ResponseEntity.ok("Password updated successfully");
    }
}
