package com.chancla.chancla_lite_auth.controller;

import com.chancla.chancla_lite_auth.config.JwtTokenProvider;
import com.chancla.chancla_lite_auth.dto.AuthResponse;
import com.chancla.chancla_lite_auth.dto.LoginRequest;
import com.chancla.chancla_lite_auth.entity.UsuarioEntity;
import com.chancla.chancla_lite_auth.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository,
                          JwtTokenProvider jwtTokenProvider,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        UsuarioEntity usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElse(null);

        if (usuario != null && passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
            String token = jwtTokenProvider.generarToken(usuario.getEmail());
            String rolNombre = usuario.getRol() != null ? usuario.getRol().getNombre() : "SIN_ROL";
            return ResponseEntity.ok(new AuthResponse(token, usuario.getEmail(), usuario.getNombre(), rolNombre));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
