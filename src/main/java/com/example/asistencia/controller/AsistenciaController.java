package com.example.asistencia.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.asistencia.service.AsistenciaService;
import com.example.asistencia.dto.AsistenciaDTO;
import com.example.asistencia.dto.AsistenciaResponseDTO;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/asistencias")
@CrossOrigin(origins = "http://localhost:5173") // ajustar para producción
public class AsistenciaController {

    private final AsistenciaService service;

    public AsistenciaController(AsistenciaService service) {
        this.service = service;
    }

    // -----------------------
    // CREATE
    // -----------------------
    @PostMapping
    public ResponseEntity<AsistenciaResponseDTO> crear(@Valid @RequestBody AsistenciaDTO dto) {
        // crea y devuelve el DTO resultante
        var saved = service.crear(dto);
        var dtoOpt = service.obtenerPorIdDTO(saved.getId());
        return dtoOpt
                .map(d -> ResponseEntity.status(HttpStatus.CREATED).body(d))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CREATED).build());
    }

    // -----------------------
    // LIST / GET
    // -----------------------
    // GET /api/asistencias  => devuelve lista de AsistenciaResponseDTO
    @GetMapping
    public ResponseEntity<List<AsistenciaResponseDTO>> listarTodas() {
        List<AsistenciaResponseDTO> lista = service.listarTodasDTO();
        return ResponseEntity.ok(lista);
    }

    // GET /api/asistencias/{id}
    @GetMapping("/{id}")
    public ResponseEntity<AsistenciaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorIdDTO(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/asistencias/estudiante/{id}
    @GetMapping("/estudiante/{id}")
    public ResponseEntity<List<AsistenciaResponseDTO>> porEstudiante(@PathVariable Long id) {
        List<AsistenciaResponseDTO> lista = service.listarPorEstudianteDTO(id);
        return ResponseEntity.ok(lista);
    }

    // GET /api/asistencias/grupo/{grupoId}/fecha/{fecha}
    @GetMapping("/grupo/{grupoId}/fecha/{fecha}")
    public ResponseEntity<List<AsistenciaResponseDTO>> porGrupoFecha(
            @PathVariable Long grupoId,
            @PathVariable String fecha) {
        LocalDate f = LocalDate.parse(fecha); // formato yyyy-MM-dd
        List<AsistenciaResponseDTO> lista = service.listarPorGrupoYFechaDTO(grupoId, f);
        return ResponseEntity.ok(lista);
    }

    // -----------------------
    // UPDATE (parcial: estado)
    // -----------------------
    @PutMapping("/{id}/estado")
    public ResponseEntity<AsistenciaResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String nuevoEstado = body.get("estado");
        var updated = service.actualizarEstado(id, nuevoEstado);
        var dtoOpt = service.obtenerPorIdDTO(updated.getId());
        return dtoOpt.map(ResponseEntity::ok).orElse(ResponseEntity.ok().build());
    }

    // -----------------------
    // DELETE
    // -----------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAsistencia(@PathVariable Long id) {
        service.eliminarAsistencia(id);
        return ResponseEntity.noContent().build();
    }
}
