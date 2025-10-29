package com.example.asistencia.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.asistencia.model.Grupo;

public interface GrupoRepository extends JpaRepository<Grupo, Long> { }
