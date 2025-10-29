package com.example.asistencia.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173") // permite peticiones desde tu frontend
public class AuthController {

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Map<String, Object> response = new HashMap<>();

        // 🔹 Aquí podrías consultar tu base de datos para validar usuario/contraseña
        if ("admin".equals(username) && "123".equals(password)) {
            response.put("token", UUID.randomUUID().toString());
            response.put("user", Map.of("nombre", "Administrador", "rol", "ADMIN"));
        } else if ("luis".equals(username) && "123".equals(password)) {
            response.put("token", UUID.randomUUID().toString());
            response.put("user", Map.of("nombre", "Luis Ángel", "rol", "USER"));
        } else {
            throw new RuntimeException("Credenciales inválidas");
        }

        return response;
    }
}
