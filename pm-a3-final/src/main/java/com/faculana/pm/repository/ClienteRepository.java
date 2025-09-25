package com.faculana.pm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.faculana.pm.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
