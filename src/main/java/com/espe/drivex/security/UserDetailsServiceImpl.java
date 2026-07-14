package com.espe.drivex.security;

import com.espe.drivex.entity.Usuario;
import com.espe.drivex.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Carga el usuario desde la base de datos usando el email como identificador.
 * Spring Security llama a este servicio al intentar autenticar.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Intentando autenticar usuario con email: {}", email);

        Usuario usuario = usuarioRepository.findByEmailAndActivoTrue(email)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado o inactivo: {}", email);
                    return new UsernameNotFoundException("Usuario no encontrado: " + email);
                });

        // El rol se almacena como "ADMIN" o "USER"; Spring Security requiere el prefijo ROLE_
        String rolNombre = usuario.getRol().getNombre().toUpperCase();
        String rol = "ROLE_" + ("PROPIETARIO".equals(rolNombre) ? "USER" : rolNombre);

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(rol)))
                .build();
    }
}
