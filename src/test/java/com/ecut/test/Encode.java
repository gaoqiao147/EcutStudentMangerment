package com.ecut.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class Encode {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void setPasswordEncoder() {
        System.out.println("----------------");
        System.out.println(passwordEncoder.encode("weizhou147"));
//        System.out.println(passwordEncoder.matches("123456","$2a$10$4hhKRSEaCEdbjSJL3eGNme44zuHut8esi0Os8TFhCQmcsm1zaW3MK"));
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        String password = bCryptPasswordEncoder.encode("123456");
//        System.out.println(password);
    }



}
