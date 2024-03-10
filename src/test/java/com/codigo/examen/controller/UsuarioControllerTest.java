package com.codigo.examen.controller;

import com.codigo.examen.config.TestSecurityConfig;
import com.codigo.examen.entity.Rol;
import com.codigo.examen.entity.Usuario;
import com.codigo.examen.repository.RolRepository;
import com.codigo.examen.repository.UsuarioRepository;
import com.codigo.examen.service.impl.UsuarioServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class UsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private RolRepository rolRepository;
    @InjectMocks
    private UsuarioServiceImpl usuarioService;
    @Autowired
    private ObjectMapper objectMapper;
    private Rol rol = new Rol();
    private Set<Rol> roles = new HashSet<>();
    private Usuario usuario;
    @BeforeEach
    void setup()
    {
        rol.setIdRol(1L);
        rol.setNombreRol("Admin");
        roles.add(rol);
        usuario=Usuario.builder()
                .idUsuario(1L)
                .email("juan123@hotmail.com")
                .username("juan123")
                .password("123456")
                .enabled(true)
                .telefono("123456789")
                .roles(roles)
                .build();
    }
    @Test
    @DisplayName("Test para crear un Usuario")
    void TestCreateUser() throws Exception {
        Usuario newUser = new Usuario();
        newUser.setIdUsuario(2L);
        newUser.setUsername("juan123");
        newUser.setPassword("12345678");
        Usuario savedUser = new Usuario();
        savedUser.setIdUsuario(newUser.getIdUsuario());
        savedUser.setUsername(newUser.getUsername());
        savedUser.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));
        given(usuarioRepository.findByUsername(newUser.getUsername())).willReturn(Optional.empty());
        given(usuarioRepository.save(any(Usuario.class))).willReturn(savedUser);
        ResultActions resultActions = mockMvc.perform(post("/ms-examen/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)));
        resultActions.andDo(print()).andExpect(status().isOk());
    }
    @Test
    @DisplayName("Obtener Usuario por ID")
    public void testGetUsuarioById() throws Exception {
        given(usuarioRepository.findById(1L)).willReturn(Optional.of(usuario));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/ms-examen/v1/usuarios/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    @DisplayName("Eliminar Usuario por ID")
    void testDeleteUsuarioById() throws Exception {
        given(usuarioRepository.findById(1L)).willReturn(Optional.of(usuario));
        mockMvc.perform(delete("/ms-examen/v1/usuarios/{id}", 1L))
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("Actualizar Usuario por ID")
    void testUpdateUsuarioById() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setUsername("ALEXI123");
        given(usuarioRepository.findById(1L)).willReturn(Optional.of(new Usuario()));
        mockMvc.perform(put("/ms-examen/v1/usuarios/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk());
    }
}
