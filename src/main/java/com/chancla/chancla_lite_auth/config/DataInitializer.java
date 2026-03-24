package com.chancla.chancla_lite_auth.config;

import com.chancla.chancla_lite_auth.entity.RolEntity;
import com.chancla.chancla_lite_auth.entity.UsuarioEntity;
import com.chancla.chancla_lite_auth.enums.RolNombre;
import com.chancla.chancla_lite_auth.repository.RolRepository;
import com.chancla.chancla_lite_auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        try {
            System.out.println(">>> [DEBUG] INICIANDO DATA INITIALIZER...");
            // 1. Crear roles básicos if none
            crearRolSiNoExiste(RolNombre.ADMINISTRADOR);
            crearRolSiNoExiste(RolNombre.VENDEDOR);
            crearRolSiNoExiste(RolNombre.JEFE);

            // 2. Crear usuario admin inicial para pruebas
            String adminEmail = "admin@majostep.com";
            if (usuarioRepository.findByEmail(adminEmail).isEmpty()) {
                System.out.println(">>> [DEBUG] Buscando rol ADMINISTRADOR...");
                RolEntity adminRol = rolRepository.findByNombre(RolNombre.ADMINISTRADOR.name())
                        .orElseThrow(() -> new RuntimeException("Error: Rol ADMINISTRADOR no encontrado en la DB"));
                
                UsuarioEntity admin = new UsuarioEntity();
                admin.setNombre("Administrador MajoStep");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRol(adminRol);
                admin.setActivo(true);
                admin.setSueldoDiario(0.0); // Añadido para evitar errores de validación si existen
                
                usuarioRepository.save(admin);
                System.out.println(">>> [DEBUG] ÉXITO: Usuario ADMINISTRADOR creado: " + adminEmail + " / admin123");
            } else {
                System.out.println(">>> [DEBUG] INFO: El usuario ADMIN ya existe.");
            }
        } catch (Exception e) {
            System.err.println(">>> [DEBUG] ERROR CRÍTICO EN DATA INITIALIZER: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void crearRolSiNoExiste(RolNombre rolNombre) {
        if (rolRepository.findByNombre(rolNombre.name()).isEmpty()) {
            RolEntity rol = new RolEntity();
            rol.setNombre(rolNombre.name());
            rolRepository.save(rol);
            System.out.println("Rol creado automáticamente: " + rolNombre.name());
        }
    }
}
