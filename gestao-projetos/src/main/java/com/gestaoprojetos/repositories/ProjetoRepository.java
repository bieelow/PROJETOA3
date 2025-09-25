package com.gestaoprojetos.repositories;

import com.gestaoprojetos.models.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
}
