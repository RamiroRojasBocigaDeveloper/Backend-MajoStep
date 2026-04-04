package com.chancla.chancla_lite_auth.config;

import com.chancla.chancla_lite_auth.entity.RolEntity;
import com.chancla.chancla_lite_auth.entity.UsuarioEntity;
import com.chancla.chancla_lite_auth.enums.RolNombre;
import com.chancla.chancla_lite_auth.repository.RolRepository;
import com.chancla.chancla_lite_auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
// import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    // @Transactional
    public void init() {
        try {
            System.out.println(">>> [DEBUG] INICIANDO DATA INITIALIZER...");

            // 1. Crear roles básicos si no existen
            crearRolSiNoExiste(RolNombre.ADMINISTRADOR);
            crearRolSiNoExiste(RolNombre.VENDEDOR);
            crearRolSiNoExiste(RolNombre.JEFE);

            // 2. Crear usuario admin inicial
            String adminEmail = "admin@majostep.com";

            if (usuarioRepository.findByEmail(adminEmail).isEmpty()) {

                RolEntity adminRol = rolRepository.findByNombre(RolNombre.ADMINISTRADOR.name())
                        .orElseThrow(() -> new RuntimeException("Rol ADMINISTRADOR no encontrado"));

                UsuarioEntity admin = new UsuarioEntity();
                admin.setNombre("Administrador MajoStep");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRol(adminRol);
                admin.setActivo(true);
                admin.setSueldoDiario(0.0);

                usuarioRepository.save(admin);

                System.out.println(">>> [DEBUG] ADMIN creado correctamente");
            } else {
                System.out.println(">>> [DEBUG] ADMIN ya existe");
            }

        } catch (Exception e) {
            System.err.println(">>> [DEBUG] ERROR EN DATA INITIALIZER: " + e.getMessage());
        }
    }

    private void crearRolSiNoExiste(RolNombre rolNombre) {
        if (rolRepository.findByNombre(rolNombre.name()).isEmpty()) {
            RolEntity rol = new RolEntity();
            rol.setNombre(rolNombre.name());
            rolRepository.save(rol);
        }
    }
}