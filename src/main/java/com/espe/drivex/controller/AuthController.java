package com.espe.drivex.controller;

import com.espe.drivex.dto.AuthResponse;
import com.espe.drivex.dto.UsuarioRequest;
import com.espe.drivex.dto.UsuarioResponse;
import com.espe.drivex.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro de usuarios y verificación de sesión")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Endpoint público: registra un nuevo usuario con rol USER.
     * No requiere autenticación.
     */
    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario (público, sin autenticación)")
    public ResponseEntity<UsuarioResponse> register(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(request));
    }

    /**
     * Endpoint protegido: devuelve los datos del usuario autenticado.
     * Usar con el botón Authorize en Swagger para verificar el login.
     * Devuelve 401 si no hay credenciales, 403 si el rol no tiene permiso.
     */
    @GetMapping("/me")
    @Operation(summary = "Ver usuario autenticado (requiere credenciales válidas)")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<AuthResponse> me(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.joining(", "));

        AuthResponse response = new AuthResponse(
                authentication.getName(),
                authentication.getAuthorities().stream()
                        .map(a -> a.getAuthority())
                        .collect(Collectors.toList()),
                "Autenticación exitosa"
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint protegido: simula el login devolviendo un 200 si las credenciales
     * son válidas. Spring Security valida el usuario antes de llegar aquí.
     * Devuelve 401 automáticamente si las credenciales son incorrectas.
     */
    @PostMapping("/login")
    @Operation(summary = "Verificar login (requiere credenciales en Authorize)")
    @SecurityRequirement(name = "basicAuth")
    public ResponseEntity<AuthResponse> login(Authentication authentication) {
        AuthResponse response = new AuthResponse(
                authentication.getName(),
                authentication.getAuthorities().stream()
                        .map(a -> a.getAuthority())
                        .collect(Collectors.toList()),
                "Login exitoso"
        );
        return ResponseEntity.ok(response);
    }
}