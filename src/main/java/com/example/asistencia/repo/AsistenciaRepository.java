package com.example.asistencia.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.asistencia.model.Asistencia;
import java.time.LocalDate;
import java.util.List;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByEstudianteId(Long estudianteId);
    List<Asistencia> findByGrupoIdAndFecha(Long grupoId, LocalDate fecha);
    boolean existsByFechaAndEstudianteIdAndGrupoId(LocalDate fecha, Long estudianteId, Long grupoId);
}
