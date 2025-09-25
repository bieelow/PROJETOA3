package com.gestaoprojetos.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    public static void main(String[] args) {
        BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
        String raw = "123";
        String hash = enc.encode(raw);
        System.out.println(hash);
    }
}
