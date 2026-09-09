package com.komal.carehub.service;

import com.komal.carehub.dto.AuthRequest;
import com.komal.carehub.dto.AuthResponse;
import com.komal.carehub.dto.RegisterRequest;
import com.komal.carehub.dto.ResetPasswordRequest;

public interface AuthService {
    AuthResponse login(AuthRequest request);
    AuthResponse register(RegisterRequest request);
    void forgotPassword(String email);
    void verifyOtp(String email, String otp);
    void resetPassword(ResetPasswordRequest request);
}

