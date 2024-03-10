package com.codigo.examen.service;

import com.codigo.examen.service.impl.JWTServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JWTServiceTest {
    private JWTService jwtService;

    @BeforeEach
    void setUp(){
        jwtService = new JWTServiceImpl();
    }
    @Test
    void testGenerateToken() {
        UserDetails userDetails = new User("testuser", "password", new ArrayList<>());
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
    }
    @Test
    void testValidateToken_ValidToken() {
        UserDetails userDetails = new User("testuser", "password", new ArrayList<>());
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.validateToken(token, userDetails));
    }
    @Test
    void testExtractUserName() {
        String token = jwtService.generateToken(new User("testuser", "password", new ArrayList<>()));
        String extractedUsername = jwtService.extractUserName(token);
        assertEquals("testuser", extractedUsername);
    }
}
