package com.codigo.examen.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    private boolean accountnonexpire = false;

    @Column(nullable = false)
    private boolean accountnonlocked = false;

    @Column(nullable = false)
    private boolean credentialsnonexpired = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_rol",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol"))
    private Set<Rol> roles = new HashSet<>();
    public Set<String> getRolesNames() {
        return roles.stream().map(Rol::getNombreRol).collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombreRol()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountnonexpire;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountnonlocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsnonexpired;
    }
}