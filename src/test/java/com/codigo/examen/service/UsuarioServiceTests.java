package com.codigo.examen.service;

import com.codigo.examen.entity.Rol;
import com.codigo.examen.entity.Usuario;
import com.codigo.examen.repository.RolRepository;
import com.codigo.examen.repository.UsuarioRepository;
import com.codigo.examen.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTests {
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private RolRepository rolRepository;
    @InjectMocks
    private UsuarioServiceImpl usuarioService;
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
    @DisplayName("test para obtener un usuario por id")
    void TestgetUsuarioById(){
        given(usuarioRepository.findById(1L)).willReturn(Optional.of(usuario));
        Usuario usuario1= usuarioService.getUsuarioById(usuario.getIdUsuario()).getBody();
        ResponseEntity<Usuario>response=usuarioService.getUsuarioById(usuario.getIdUsuario());
        assertThat(usuario1).isNotNull();
        assertThat(usuario1.getUsername()).isEqualTo("juan123");
        assertThat(response.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @DisplayName("Test para crear un usuario si no existe")
    void testCreateUsuario_WithNonExistingUser() {
        Usuario newUser = new Usuario();
        newUser.setIdUsuario(2L);
        newUser.setUsername("juan123");
        newUser.setPassword("12345678");
        when(usuarioRepository.findByUsername(newUser.getUsername())).thenReturn(Optional.empty());
        ResponseEntity<Usuario> response = usuarioService.createUsuario(newUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    @DisplayName("Test para crear un usuario si existe")
    void testCreateUsuario_WithExistingUser() {
        Usuario newUser = new Usuario();
        newUser.setIdUsuario(2L);
        newUser.setUsername("juan123");
        newUser.setPassword("12345678");
        when(usuarioRepository.findByUsername(newUser.getUsername())).thenReturn(Optional.of(usuario));
        ResponseEntity<Usuario> response = usuarioService.createUsuario(newUser);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    @DisplayName("test para actualizar un usuario existente")
    void testUpdateUsuario_WithExistingUser() {
        Long userId = 1L;
        Usuario updatedUser = new Usuario();
        updatedUser.setIdUsuario(userId);
        updatedUser.setUsername("updatedUser");
        updatedUser.setPassword(usuario.getPassword());
        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByUsername(updatedUser.getUsername())).thenReturn(Optional.empty());
        when(usuarioRepository.save(updatedUser)).thenReturn(updatedUser);
        ResponseEntity<Usuario> response = usuarioService.updateUsuario(userId, updatedUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
    }
    @Test
    @DisplayName("test para borrar un usuario existente")
    void testDeleteUsuario_WithExistingUser() {
        given(usuarioRepository.findById(1L)).willReturn(Optional.of(usuario));
        ResponseEntity<Usuario> response = usuarioService.deleteUsuario(1L);
        assertThat(response.getStatusCodeValue()).isEqualTo(204);
    }

    @Test
    @DisplayName("test para borrar un usuario que no existe")
    void testDeleteUsuario_WithNonExistingUser() {
        given(usuarioRepository.findById(1L)).willReturn(Optional.empty());
        ResponseEntity<Usuario> response = usuarioService.deleteUsuario(1L);
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }
    @Test
    @DisplayName("test para probar UserDetailsService")
    void testUserDetailsService()
    {
        String username = "juan123";
        Usuario existingUser = new Usuario();
        existingUser.setUsername(username);
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));
        UserDetails userDetails = usuarioService.userDetailsService().loadUserByUsername(username);
        assertEquals(existingUser.getUsername(), userDetails.getUsername());
    }
}
