package com.programandoconjava.application.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * The purpose of this class is to create an encrypted password 
 * manually
 */
public class PasswordGenerator {
    
    private static final Logger logger = LogManager.getLogger(PasswordGenerator.class);

    public static void main(String[] args) {
        
        String password = ""; // Put the password to be encrypted here.

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String passwordEncoded = encoder.encode(password);

        logger.info("Encoded password: {}", passwordEncoded);
    }
}
