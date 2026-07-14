package com.espe.drivex.security;

import com.espe.drivex.entity.Usuario;
import com.espe.drivex.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Carga el usuario desde la BD usando el email como identificador.
 * Mapea los roles de negocio (ADMIN, PROPIETARIO, cualquier otro → USER)
 * al formato que Spring Security espera (ROLE_ADMIN, ROLE_PROPIETARIO, ROLE_USER).
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

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado: {}", email);
                    return new UsernameNotFoundException("Usuario no encontrado con email: " + email);
                });

        String rolNombre = usuario.getRol().getNombre().toUpperCase();

        // Mapeo de roles de negocio a roles de seguridad
        String rolSeguridad = switch (rolNombre) {
            case "ADMIN"      -> "ADMIN";
            case "PROPIETARIO" -> "PROPIETARIO";
            default           -> "USER";
        };

        log.debug("Usuario {} autenticado con rol: {}", email, rolSeguridad);

        return User.withUsername(usuario.getEmail())
                .password(usuario.getPassword())
                .roles(rolSeguridad)                          // agrega el prefijo ROLE_ automáticamente
                .disabled(!Boolean.TRUE.equals(usuario.getActivo()))
                .build();
    }
}
