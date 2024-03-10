package com.codigo.examen.config;

import com.codigo.examen.service.JWTService;
import com.codigo.examen.service.UsuarioService;
import com.codigo.examen.service.impl.JWTServiceImpl;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestSecurityConfig {
    @Bean
    public UsuarioService usuarioService() {
        return Mockito.mock(UsuarioService.class);
    }
    @Bean
    public JWTService jwtService() {
        return new JWTServiceImpl();
    }
}
