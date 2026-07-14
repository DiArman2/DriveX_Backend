package com.espe.drivex.service;

import com.espe.drivex.dto.UsuarioRequest;
import com.espe.drivex.dto.UsuarioResponse;
import com.espe.drivex.entity.Rol;
import com.espe.drivex.entity.Usuario;
import com.espe.drivex.exception.ConflictException;
import com.espe.drivex.exception.ResourceNotFoundException;
import com.espe.drivex.repository.RolRepository;
import com.espe.drivex.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ── READ ─────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        log.info("Listando todos los usuarios activos");
        return usuarioRepository.findAllByActivoTrue()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        log.info("Buscando usuario con id: {}", id);
        return toResponse(findActivo(id));
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> buscarPorNombre(String texto) {
        log.info("Buscando usuarios por nombre: {}", texto);
        return usuarioRepository
                .findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(texto, texto)
                .stream().filter(Usuario::getActivo)
                .map(this::toResponse).collect(Collectors.toList());
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    public UsuarioResponse guardar(UsuarioRequest request) {
        log.info("Registrando nuevo usuario: {}", request.getEmail());

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Ya existe un usuario registrado con el email: " + request.getEmail());
        }

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + request.getRolId()));

        Usuario usuario = new Usuario();
        usuario.setRol(rol);
        usuario.setNombres(request.getNombres());
        usuario.setApellidos(request.getApellidos());
        usuario.setEmail(request.getEmail());
        // Contraseña hasheada con BCrypt — nunca se guarda en texto plano
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setTelefono(request.getTelefono());
        usuario.setCedula(request.getCedula());
        usuario.setFechaNacimiento(request.getFechaNacimiento());
        usuario.setActivo(true);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setFechaActualizacion(LocalDateTime.now());

        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario creado con id: {}", guardado.getId());
        return toResponse(guardado);
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    public UsuarioResponse actualizar(Long id, UsuarioRequest request) {
        log.info("Actualizando usuario con id: {}", id);
        Usuario usuario = findActivo(id);

        // Verificar email duplicado en otro usuario
        usuarioRepository.findByEmail(request.getEmail()).ifPresent(u -> {
            if (!u.getId().equals(id)) {
                throw new ConflictException("El email ya está en uso por otro usuario");
            }
        });

        Rol rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + request.getRolId()));

        usuario.setRol(rol);
        usuario.setNombres(request.getNombres());
        usuario.setApellidos(request.getApellidos());
        usuario.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        usuario.setTelefono(request.getTelefono());
        usuario.setCedula(request.getCedula());
        usuario.setFechaNacimiento(request.getFechaNacimiento());
        usuario.setFechaActualizacion(LocalDateTime.now());

        return toResponse(usuarioRepository.save(usuario));
    }

    // ── DELETE (lógico) ───────────────────────────────────────────────────────

    public void eliminar(Long id) {
        log.info("Desactivando usuario con id: {}", id);
        Usuario usuario = findActivo(id);
        usuario.setActivo(false);
        usuario.setFechaActualizacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Usuario findActivo(Long id) {
        return usuarioRepository.findById(id)
                .filter(Usuario::getActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(
                u.getId(),
                u.getRol().getId(),
                u.getRol().getNombre(),
                u.getNombres(),
                u.getApellidos(),
                u.getEmail(),
                u.getTelefono(),
                u.getCedula(),
                u.getFechaNacimiento(),
                u.getActivo(),
                u.getFechaCreacion(),
                u.getFechaActualizacion()
        );
    }
}
