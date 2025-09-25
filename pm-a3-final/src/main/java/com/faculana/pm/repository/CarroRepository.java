package com.faculana.pm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.faculana.pm.model.Carro;

public interface CarroRepository extends JpaRepository<Carro, Long> {
}
