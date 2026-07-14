package com.espe.drivex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de Spring Security.
 *
 * Roles:
 *  - ADMIN      : acceso completo
 *  - PROPIETARIO: puede consultar todo, gestionar sus vehículos e imágenes
 *  - USER       : puede consultar todo, crear reservas y pagos
 *
 * Sesión: STATELESS — sin estado del lado del servidor.
 * Autenticación: HTTP Basic (botón Authorize en Swagger).
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                // ── Público ─────────────────────────────────────────────────
                .requestMatchers(
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**"
                ).permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()

                // ── Auth: cualquier usuario autenticado ──────────────────────
                .requestMatchers("/api/auth/**").authenticated()

                // ── Consulta: USER, PROPIETARIO y ADMIN ─────────────────────
                .requestMatchers(HttpMethod.GET, "/api/**")
                        .hasAnyRole("USER", "PROPIETARIO", "ADMIN")

                // ── USER puede crear reservas y pagos ────────────────────────
                .requestMatchers(HttpMethod.POST, "/api/reservas/**", "/api/pagos/**")
                        .hasAnyRole("USER", "PROPIETARIO", "ADMIN")

                // ── PROPIETARIO gestiona sus vehículos e imágenes ────────────
                .requestMatchers(HttpMethod.POST,
                        "/api/vehiculos/**", "/api/imagenes-vehiculo/**")
                        .hasAnyRole("PROPIETARIO", "ADMIN")
                .requestMatchers(HttpMethod.PUT,
                        "/api/vehiculos/**", "/api/imagenes-vehiculo/**")
                        .hasAnyRole("PROPIETARIO", "ADMIN")
                .requestMatchers(HttpMethod.DELETE,
                        "/api/vehiculos/**", "/api/imagenes-vehiculo/**")
                        .hasAnyRole("PROPIETARIO", "ADMIN")

                // ── Solo ADMIN para el resto ─────────────────────────────────
                .requestMatchers(HttpMethod.POST,   "/api/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/api/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
