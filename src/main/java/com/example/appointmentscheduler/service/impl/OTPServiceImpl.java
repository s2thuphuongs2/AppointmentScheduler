package com.example.appointmentscheduler.service.impl;

import com.example.appointmentscheduler.model.UserForm;
import com.example.appointmentscheduler.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OTPServiceImpl implements OTPService {

    private final RedisTemplate<String, UserForm> redisTemplate;
    private final JavaMailSender mailSender;

    @Autowired
    public OTPServiceImpl(RedisTemplate<String, UserForm> redisTemplate, JavaMailSender mailSender) {
        this.redisTemplate = redisTemplate;
        this.mailSender = mailSender;
    }

    @Override
    public String generateAndSendOTP(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // Tạo mã OTP 6 chữ số
        UserForm userForm = new UserForm();
        userForm.setOtp(otp);
        userForm.setEmail(email);
        redisTemplate.opsForValue().set(email, userForm, 5, TimeUnit.MINUTES); // Lưu OTP trong Redis với thời hạn 5 phút
        sendOTPEmail(email, otp);
        return otp;
    }
    @Override
    public void sendOTPEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Mã OTP xác thực tài khoản");
        message.setText("Mã OTP của bạn là: " + otp);
        mailSender.send(message);
    }
    @Override
    public boolean validateOTP(String email, String otp) {
        UserForm storedOtp = redisTemplate.opsForValue().get(email);
        return storedOtp != null && otp.equals(storedOtp.getOtp());
    }
}
