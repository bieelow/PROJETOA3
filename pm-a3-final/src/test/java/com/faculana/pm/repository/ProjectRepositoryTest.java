package com.faculana.pm.repository;

import com.faculana.pm.domain.Project;
import com.faculana.pm.domain.ProjectStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProjectRepositoryTest {

  @Autowired
  private ProjectRepository repo;

  @Test
  void salvaERecuperaProjeto() {
    var p = new Project();
    p.setTitulo("Projeto A");
    p.setStatus(ProjectStatus.PLANEJADO);
    var salvo = repo.save(p);

    assertNotNull(salvo.getId());
    var recuperado = repo.findById(salvo.getId()).orElseThrow();
    assertEquals("Projeto A", recuperado.getTitulo());
  }
}
