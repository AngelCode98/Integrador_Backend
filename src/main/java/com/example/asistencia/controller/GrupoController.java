package com.example.asistencia.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.example.asistencia.model.Grupo;
import com.example.asistencia.repo.GrupoRepository;

import java.util.List;

@RestController
@RequestMapping("/api/grupos")
@CrossOrigin(origins = "http://localhost:5173")
public class GrupoController {

    private final GrupoRepository repo;

    public GrupoController(GrupoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<List<Grupo>> listarTodos() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grupo> porId(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Grupo> crear(@RequestBody Grupo g) {
        Grupo saved = repo.save(g);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
