package com.complexo.UNIFEBE_Complexo_Esportivo.controller;

import com.complexo.UNIFEBE_Complexo_Esportivo.dao.Agendamentos;
import com.complexo.UNIFEBE_Complexo_Esportivo.service.AgendamentosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/agendamentos")
@CrossOrigin(origins = "*")
public class AgendamentosController {

    private final AgendamentosService service;

    public AgendamentosController(AgendamentosService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> criar(@RequestBody Agendamentos a) {
        int res = service.inserirAgendamento(a);
        if (res == 1) {
            return ResponseEntity.ok("OK");
        } else if (res == 2) {
            return ResponseEntity.status(409).body("Conflito de agendamento. O horário já está reservado.");
        } else {
            return ResponseEntity.status(500).body("Erro ao agendar");
        }
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<String> cancelar(@PathVariable int id) {
        System.out.println("=== PUT CANCELAR AGENDAMENTO ===");
        System.out.println("ID: " + id);
        int res = service.cancelarAgendamento(id);
        System.out.println("Resultado: " + res);
        return res > 0 ? ResponseEntity.ok("OK") : ResponseEntity.status(404).body("Agendamento não encontrado ou erro ao cancelar");
    }
    
    // Rota alternativa usando POST
    @PostMapping("/{id}/cancelar")
    public ResponseEntity<String> cancelarPost(@PathVariable int id) {
        System.out.println("=== POST CANCELAR AGENDAMENTO ===");
        System.out.println("ID: " + id);
        int res = service.cancelarAgendamento(id);
        System.out.println("Resultado: " + res);
        return res > 0 ? ResponseEntity.ok("OK") : ResponseEntity.status(404).body("Agendamento não encontrado ou erro ao cancelar");
    }
    
    // Endpoint para excluir agendamento permanentemente
    @DeleteMapping("/{id}")
    public ResponseEntity<String> excluir(@PathVariable int id) {
        System.out.println("=== DELETE AGENDAMENTO ===");
        System.out.println("ID: " + id);
        int res = service.excluirAgendamento(id);
        System.out.println("Resultado: " + res);
        return res > 0 ? ResponseEntity.ok("OK") : ResponseEntity.status(404).body("Agendamento não encontrado ou erro ao excluir");
    }
    
    // Rota alternativa usando POST
    @PostMapping("/{id}/excluir")
    public ResponseEntity<String> excluirPost(@PathVariable int id) {
        System.out.println("=== POST EXCLUIR AGENDAMENTO ===");
        System.out.println("ID: " + id);
        int res = service.excluirAgendamento(id);
        System.out.println("Resultado: " + res);
        return res > 0 ? ResponseEntity.ok("OK") : ResponseEntity.status(404).body("Agendamento não encontrado ou erro ao excluir");
    }

    @GetMapping("/usuario/{id}")
    public ArrayList<Agendamentos> porUsuario(@PathVariable int id) {
        return service.consultarAgendamentosUsuario(id);
    }

    @GetMapping("/usuario/{id}/futuros")
    public ArrayList<Agendamentos> porUsuarioFuturos(@PathVariable int id) {
        return service.consultarAgendamentosUsuarioFuturos(id);
    }

    @GetMapping("/ambiente/{id}")
    public ArrayList<Agendamentos> porAmbiente(@PathVariable int id) {
        return service.consultarAgendamentosAmbiente(id);
    }

    @GetMapping("/ambiente/{id}/futuros")
    public ArrayList<Agendamentos> porAmbienteFuturos(@PathVariable int id) {
        return service.consultarAgendamentosAmbienteFuturos(id);
    }
}
