package com.faculana.pm.service;

import com.faculana.pm.domain.Project;
import com.faculana.pm.repository.ProjectRepository;
import com.faculana.pm.web.NotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Testa comportamento mínimo do ProjectService usando Mockito para isolar o repositório.
class ProjectServiceTest {

  @Test
  void listar_retornaElementosDoRepositorio() {
    var repo = Mockito.mock(ProjectRepository.class);
    when(repo.findAll()).thenReturn(List.of(new Project(), new Project()));
    var svc = new ProjectService(repo);
    assertEquals(2, svc.listar().size());
  }

  @Test
  void obter_quandoNaoExiste_disparaNotFound() {
    var repo = Mockito.mock(ProjectRepository.class);
    when(repo.findById(99L)).thenReturn(Optional.empty());
    var svc = new ProjectService(repo);
    assertThrows(NotFoundException.class, () -> svc.obter(99L));
  }

  @Test
  void criar_chamaSave() {
    var repo = Mockito.mock(ProjectRepository.class);
    var p = new Project(); p.setTitulo("X");
    when(repo.save(p)).thenReturn(p);
    var svc = new ProjectService(repo);
    assertEquals("X", svc.criar(p).getTitulo());
    verify(repo, times(1)).save(p);
  }
}
