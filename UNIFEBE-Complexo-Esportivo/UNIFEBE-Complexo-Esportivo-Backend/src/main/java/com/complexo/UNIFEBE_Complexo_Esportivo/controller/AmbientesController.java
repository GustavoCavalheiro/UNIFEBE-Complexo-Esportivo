package com.complexo.UNIFEBE_Complexo_Esportivo.controller;

import com.complexo.UNIFEBE_Complexo_Esportivo.dao.Ambientes;
import com.complexo.UNIFEBE_Complexo_Esportivo.service.AmbientesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/ambientes")
@CrossOrigin(origins = "*")
public class AmbientesController {

    private final AmbientesService service;

    public AmbientesController(AmbientesService service) {
        this.service = service;
    }

    @GetMapping
    public ArrayList<Ambientes> listar() {
        return service.listar();
    }

    @GetMapping("/nome/{nome}")
    public ArrayList<Ambientes> buscarPorNome(@PathVariable String nome) {
        return service.buscarPorNome(nome);
    }

    @PostMapping
    public ResponseEntity<String> criar(@RequestBody Ambientes ambiente) {
        int res = service.inserir(ambiente);
        return res > 0 ? ResponseEntity.ok("OK") : ResponseEntity.status(500).body("Erro");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizar(@PathVariable int id, @RequestBody Ambientes ambiente) {
        ambiente.setId_AMBIENTES(id);
        int res = service.atualizar(ambiente);
        return res > 0 ? ResponseEntity.ok("OK") : ResponseEntity.status(404).body("Não encontrado/erro");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remover(@PathVariable int id) {
        System.out.println("=== DELETE AMBIENTE ===");
        System.out.println("ID: " + id);
        int res = service.remover(id);
        System.out.println("Resultado: " + res);
        return res > 0 ? ResponseEntity.ok("OK") : ResponseEntity.status(404).body("Não encontrado/erro");
    }

    // Rota alternativa usando POST
    @PostMapping("/{id}/excluir")
    public ResponseEntity<String> removerPost(@PathVariable int id) {
        System.out.println("=== POST EXCLUIR AMBIENTE ===");
        System.out.println("ID: " + id);
        int res = service.remover(id);
        System.out.println("Resultado: " + res);
        return res > 0 ? ResponseEntity.ok("OK") : ResponseEntity.status(404).body("Não encontrado/erro");
    }
}