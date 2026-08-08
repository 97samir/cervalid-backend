package com.cervalid.platform.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
// generar un password BCrypt en consola
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashed = encoder.encode("123456");
        System.out.println(hashed);
    }
}
