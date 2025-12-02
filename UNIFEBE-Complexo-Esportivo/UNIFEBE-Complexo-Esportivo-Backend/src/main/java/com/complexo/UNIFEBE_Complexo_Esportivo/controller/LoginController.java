package com.complexo.UNIFEBE_Complexo_Esportivo.controller;

import com.complexo.UNIFEBE_Complexo_Esportivo.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "*")
public class LoginController {

    private final LoginService service;

    public LoginController(LoginService service) {
        this.service = service;
    }

    @GetMapping("/validar/{matricula}/{senha}")
    public ResponseEntity<String> verificar(
            @PathVariable int matricula,
            @PathVariable String senha) {

        return service.validarEntrada(matricula, senha)
                ? ResponseEntity.ok("Entrada validada com sucesso!")
                : ResponseEntity.badRequest().body("Credenciais inválidas!");
    }
    
    // Endpoint de logout - sempre retorna sucesso pois o logout é feito no frontend
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        System.out.println("=== LOGOUT ===");
        System.out.println("Logout realizado com sucesso");
        return ResponseEntity.ok("Logout realizado com sucesso!");
    }
    
    // Endpoint de logout via GET (alternativa)
    @GetMapping("/logout")
    public ResponseEntity<String> logoutGet() {
        System.out.println("=== LOGOUT (GET) ===");
        System.out.println("Logout realizado com sucesso");
        return ResponseEntity.ok("Logout realizado com sucesso!");
    }
}
