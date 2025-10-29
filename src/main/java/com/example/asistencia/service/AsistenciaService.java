package com.example.asistencia.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.asistencia.repo.*;
import com.example.asistencia.model.*;
import com.example.asistencia.dto.AsistenciaDTO;
import com.example.asistencia.dto.AsistenciaResponseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AsistenciaService {

    private static final Logger logger = LoggerFactory.getLogger(AsistenciaService.class);

    private final AsistenciaRepository asistenciaRepo;
    private final EstudianteRepository estudianteRepo;
    private final GrupoRepository grupoRepo;

    public AsistenciaService(AsistenciaRepository a, EstudianteRepository e, GrupoRepository g) {
        this.asistenciaRepo = a;
        this.estudianteRepo = e;
        this.grupoRepo = g;
    }

    // ------------------------------------------------
    // CREATE
    // ------------------------------------------------
    @Transactional
    public Asistencia crear(AsistenciaDTO dto) {
        // validar fecha futura
        if (dto.getFecha() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fecha requerida.");
        }
        if (dto.getFecha().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se permiten fechas futuras.");
        }

        // validar existencia estudiante y grupo
        Estudiante estudiante = estudianteRepo.findById(dto.getEstudianteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado"));
        Grupo grupo = grupoRepo.findById(dto.getGrupoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grupo no encontrado"));

        // validar duplicidad (unique business rule)
        boolean existe = asistenciaRepo.existsByFechaAndEstudianteIdAndGrupoId(dto.getFecha(), dto.getEstudianteId(), dto.getGrupoId());
        if (existe) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Asistencia ya registrada para este estudiante en esa fecha y grupo.");
        }

        // validar y parsear estado
        EstadoAsistencia estadoEnum;
        try {
            estadoEnum = EstadoAsistencia.valueOf(dto.getEstado().toUpperCase());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado no válido. Use PRESENTE, AUSENTE o JUSTIFICADO.");
        }

        // construir entidad y guardar
        Asistencia a = new Asistencia();
        a.setFecha(dto.getFecha());
        a.setEstado(estadoEnum);
        a.setObservacion(dto.getObservacion());
        a.setJustificada(Boolean.TRUE.equals(dto.getJustificada()));
        a.setEstudiante(estudiante);
        a.setGrupo(grupo);

        Asistencia saved = asistenciaRepo.save(a);
        logger.info("Asistencia creada id={} estudianteId={} grupoId={} fecha={}", saved.getId(), dto.getEstudianteId(), dto.getGrupoId(), dto.getFecha());
        return saved;
    }

    // ------------------------------------------------
    // READ - entidades
    // ------------------------------------------------
    public List<Asistencia> listarPorEstudiante(Long estudianteId) {
        return asistenciaRepo.findByEstudianteId(estudianteId);
    }

    public List<Asistencia> listarPorGrupoYFecha(Long grupoId, LocalDate fecha) {
        return asistenciaRepo.findByGrupoIdAndFecha(grupoId, fecha);
    }

    public List<Asistencia> listarTodas() {
        return asistenciaRepo.findAll();
    }

    public Optional<Asistencia> obtenerPorId(Long id) {
        return asistenciaRepo.findById(id);
    }

    // ------------------------------------------------
    // READ - DTOs (recomendado para el API)
    // ------------------------------------------------
    /**
     * Lista todas las asistencias mapeadas a DTOs (ideal para devolver en controller)
     */
    public List<AsistenciaResponseDTO> listarTodasDTO() {
        return listarTodas().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Obtener por id como DTO
     */
    public Optional<AsistenciaResponseDTO> obtenerPorIdDTO(Long id) {
        return obtenerPorId(id).map(this::mapToResponse);
    }

    /**
     * Lista por estudiante como DTOs
     */
    public List<AsistenciaResponseDTO> listarPorEstudianteDTO(Long estudianteId) {
        return listarPorEstudiante(estudianteId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    /**
     * Lista por grupo y fecha como DTOs
     */
    public List<AsistenciaResponseDTO> listarPorGrupoYFechaDTO(Long grupoId, LocalDate fecha) {
        return listarPorGrupoYFecha(grupoId, fecha).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // ------------------------------------------------
    // UPDATE (parcial)
    // ------------------------------------------------
    @Transactional
    public Asistencia actualizarEstado(Long id, String nuevoEstado) {
        Asistencia asistencia = asistenciaRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asistencia no encontrada"));

        try {
            EstadoAsistencia estadoEnum = EstadoAsistencia.valueOf(nuevoEstado.toUpperCase());
            asistencia.setEstado(estadoEnum);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado no válido. Use PRESENTE, AUSENTE o JUSTIFICADO.");
        }

        Asistencia saved = asistenciaRepo.save(asistencia);
        logger.info("Asistencia id={} actualizada estado={}", id, saved.getEstado());
        return saved;
    }

    // ------------------------------------------------
    // DELETE
    // ------------------------------------------------
    @Transactional
    public void eliminarAsistencia(Long id) {
        Asistencia asistencia = asistenciaRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Asistencia no encontrada"));
        asistenciaRepo.delete(asistencia);
        logger.info("Asistencia id={} eliminada", id);
    }

    // ------------------------------------------------
    // Mapping: entidad -> DTO (manual, completo)
    // ------------------------------------------------
    private AsistenciaResponseDTO mapToResponse(Asistencia a) {
        AsistenciaResponseDTO r = new AsistenciaResponseDTO();

        // Fecha: si tu DTO tiene LocalDate, usa a.getFecha(); si es String, formatea con toString()
        r.setId(a.getId());
        r.setFecha(a.getFecha()); // espera LocalDate en DTO; si tu DTO usa String, reemplaza por a.getFecha().toString()
        r.setEstado(a.getEstado() != null ? a.getEstado().name() : null);
        r.setObservacion(a.getObservacion());
        r.setJustificada(a.getJustificada());

        if (a.getEstudiante() != null) {
            r.setEstudianteId(a.getEstudiante().getId());
            r.setEstudianteNombre(a.getEstudiante().getNombre());
            r.setEstudianteIdentificacion(a.getEstudiante().getIdentificacion()); // <-- añadido
        }

        if (a.getGrupo() != null) {
            r.setGrupoId(a.getGrupo().getId());
            r.setGrupoNombre(a.getGrupo().getNombre());
            r.setGrupoMateria(a.getGrupo().getMateria()); // <-- añadido
        }

        return r;
    }
}
