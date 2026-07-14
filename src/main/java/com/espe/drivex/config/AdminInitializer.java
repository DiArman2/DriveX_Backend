package com.espe.drivex.config;

import com.espe.drivex.entity.Rol;
import com.espe.drivex.entity.Usuario;
import com.espe.drivex.repository.RolRepository;
import com.espe.drivex.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Crea el usuario administrador al iniciar la aplicación si aún no existe.
 * Las credenciales se obtienen desde variables de entorno (.env).
 */
@Component
public class AdminInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInitializer.class);

    private final UsuarioRepository repository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.nombres}")
    private String adminNombres;

    @Value("${app.admin.apellidos}")
    private String adminApellidos;

    public AdminInitializer(UsuarioRepository repository,
                            RolRepository rolRepository,
                            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (repository.existsByEmail(adminEmail)) {
            log.info("AdminInitializer: el usuario admin ya existe -> {}", adminEmail);
            return;
        }

        // Buscar o crear el rol ADMIN
        Rol rolAdmin = rolRepository.findByNombre("ADMIN")
                .orElseGet(() -> {
                    log.warn("AdminInitializer: rol ADMIN no encontrado, creando...");
                    Rol r = new Rol();
                    r.setNombre("ADMIN");
                    r.setDescripcion("Administrador del sistema");
                    r.setActivo(true);
                    r.setFechaCreacion(LocalDateTime.now());
                    return rolRepository.save(r);
                });

        Usuario admin = new Usuario();
        admin.setNombres(adminNombres);
        admin.setApellidos(adminApellidos);
        admin.setEmail(adminEmail);
        // Contraseña hasheada con BCrypt — nunca se guarda en texto plano
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRol(rolAdmin);
        admin.setActivo(true);
        admin.setFechaCreacion(LocalDateTime.now());
        admin.setFechaActualizacion(LocalDateTime.now());

        repository.save(admin);
        log.info("AdminInitializer: usuario admin creado exitosamente -> {}", adminEmail);
    }
}
