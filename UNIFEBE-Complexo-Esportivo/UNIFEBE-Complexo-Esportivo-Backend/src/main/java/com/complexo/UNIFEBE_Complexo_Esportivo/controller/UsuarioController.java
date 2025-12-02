package com.complexo.UNIFEBE_Complexo_Esportivo.controller;

import com.complexo.UNIFEBE_Complexo_Esportivo.dao.Usuario;
import com.complexo.UNIFEBE_Complexo_Esportivo.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public ArrayList<Usuario> listar() {
        return service.listarTodos();
    }

    @GetMapping("/tipo/{tipo}")
    public ArrayList<Usuario> listarPorTipo(@PathVariable char tipo) {
        return service.listarPorTipo(tipo);
    }


    @GetMapping("/{matricula}")
    public ResponseEntity<Usuario> buscar(@PathVariable int matricula) {
        Usuario u = service.buscarPorMatricula(matricula);
        return u == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(u);
    }

    @GetMapping("/nome/{nome}")
    public ArrayList<Usuario> buscarPorNome(@PathVariable String nome) {
        return service.buscarPorNome(nome);
    }

    @PostMapping
    public ResponseEntity<String> criar(@RequestBody Usuario usuario) {
        System.out.println("=== CRIAR USUARIO ===");
        System.out.println("Nome: " + usuario.getNome());
        System.out.println("Matricula: " + usuario.getMatricula());
        
        int res = service.inserir(usuario);


        if (res == 1) {
            return ResponseEntity.ok("OK");
        } else if (res == -1) {
            return ResponseEntity.status(409).body("Matrícula já cadastrada no sistema!");
        } else {
            return ResponseEntity.status(500).body("Erro ao inserir usuário");
        }
    }

    @DeleteMapping("/{matricula}")
    public ResponseEntity<String> remover(@PathVariable int matricula) {
        System.out.println("=== DELETE USUARIO ===");
        System.out.println("Matricula: " + matricula);
        int res = service.removerPorMatricula(matricula);

        System.out.println("Resultado: " + res);
        return res > 0 ? ResponseEntity.ok("OK") : ResponseEntity.status(404).body("Usuário não encontrado ou erro ao remover");
    }

    // Rota alternativa usando POST
    @PostMapping("/{matricula}/excluir")
    public ResponseEntity<String> removerPost(@PathVariable int matricula) {
        System.out.println("=== POST EXCLUIR USUARIO ===");
        System.out.println("Matricula: " + matricula);
        int res = service.removerPorMatricula(matricula);
        System.out.println("Resultado: " + res);
        return res > 0 ? ResponseEntity.ok("OK") : ResponseEntity.status(404).body("Usuário não encontrado ou erro ao remover");
    }
}