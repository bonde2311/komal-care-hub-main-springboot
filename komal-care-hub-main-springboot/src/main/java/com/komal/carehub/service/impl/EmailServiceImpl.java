package com.komal.carehub.service.impl;

import com.komal.carehub.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendOtpEmail(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(toEmail);
            helper.setSubject("Password Reset Request - Komal Medical & Agency");
            
            String htmlContent = buildEmailTemplate(otp);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String buildEmailTemplate(String otp) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Password Reset Request</title>\n" +
                "</head>\n" +
                "<body style=\"margin:0; padding:20px; background-color:#f4f7f6; font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; color:#333;\">\n" +
                "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 15px rgba(0,0,0,0.05);\">\n" +
                "        \n" +
                "        <!-- Header -->\n" +
                "        <div style=\"background: linear-gradient(135deg, #059669 0%, #047857 100%); padding: 30px 20px; text-align: center;\">\n" +
                "            <h1 style=\"margin: 0; color: #ffffff; font-size: 24px; font-weight: 700;\">\uD83D\uDD10 Password Reset Request</h1>\n" +
                "            <p style=\"margin: 8px 0 0 0; color: rgba(255,255,255,0.9); font-size: 14px;\">Komal Medical & Agency</p>\n" +
                "        </div>\n" +
                "\n" +
                "        <!-- Body -->\n" +
                "        <div style=\"padding: 30px;\">\n" +
                "            <p style=\"margin: 0 0 20px 0; font-size: 16px; line-height: 1.5;\">Dear User,</p>\n" +
                "            <p style=\"margin: 0 0 20px 0; font-size: 16px; line-height: 1.5;\">We received a request to reset your password. Please use the OTP code below to proceed with resetting your password.</p>\n" +
                "\n" +
                "            <!-- OTP Box -->\n" +
                "            <div style=\"border: 2px dashed #059669; border-radius: 8px; padding: 25px; text-align: center; margin: 30px 0; background-color: #f0fdf4;\">\n" +
                "                <p style=\"margin: 0 0 10px 0; font-size: 14px; font-weight: 600; color: #059669;\">Your OTP Code</p>\n" +
                "                <div style=\"font-size: 36px; font-weight: 700; letter-spacing: 8px; color: #059669; margin: 0;\">" + otp + "</div>\n" +
                "                <p style=\"margin: 15px 0 0 0; font-size: 13px; color: #6b7280;\">This code will expire in <strong>10 minutes</strong></p>\n" +
                "            </div>\n" +
                "\n" +
                "            <!-- Warning Box -->\n" +
                "            <div style=\"background-color: #fffbeb; border-left: 4px solid #f59e0b; padding: 15px; border-radius: 4px; margin-bottom: 30px;\">\n" +
                "                <p style=\"margin: 0 0 8px 0; font-weight: 600; font-size: 14px; color: #92400e;\">⚠️ Security Notice:</p>\n" +
                "                <ul style=\"margin: 0; padding-left: 20px; font-size: 13px; color: #92400e; line-height: 1.5;\">\n" +
                "                    <li>Never share this OTP with anyone</li>\n" +
                "                    <li>Komal Medical will never ask for your OTP</li>\n" +
                "                    <li>If you didn't request this, please ignore this email</li>\n" +
                "                </ul>\n" +
                "            </div>\n" +
                "\n" +
                "            <!-- Steps -->\n" +
                "            <p style=\"margin: 0 0 10px 0; font-size: 14px; font-weight: 600;\">To reset your password:</p>\n" +
                "            <ol style=\"margin: 0 0 20px 0; padding-left: 20px; font-size: 14px; line-height: 1.5; color: #4b5563;\">\n" +
                "                <li>Enter the OTP code above</li>\n" +
                "                <li>Create a new strong password</li>\n" +
                "                <li>Confirm your new password</li>\n" +
                "            </ol>\n" +
                "\n" +
                "            <p style=\"margin: 0; font-size: 13px; color: #6b7280; line-height: 1.5;\">If you didn't request a password reset, you can safely ignore this email. Your password will remain unchanged.</p>\n" +
                "        </div>\n" +
                "\n" +
                "        <!-- Footer -->\n" +
                "        <div style=\"background-color: #f9fafb; border-top: 1px solid #e5e7eb; padding: 20px; text-align: center;\">\n" +
                "            <p style=\"margin: 0 0 10px 0; font-size: 12px; color: #9ca3af;\">This is an automated message from Komal Medical & Agency</p>\n" +
                "            <p style=\"margin: 0 0 10px 0; font-size: 12px; color: #9ca3af;\">Contact us: <a href=\"mailto:bondegaurav2311@gmail.com\" style=\"color: #059669; text-decoration: none;\">bondegaurav2311@gmail.com</a> | Phone: 9637595795</p>\n" +
                "            <p style=\"margin: 0; font-size: 12px; color: #9ca3af;\">© 2026 Komal Medical & Agency. All rights reserved.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}

