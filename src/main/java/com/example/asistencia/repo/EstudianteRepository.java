package com.example.asistencia.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.asistencia.model.Estudiante;
import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByIdentificacion(String identificacion);
}
