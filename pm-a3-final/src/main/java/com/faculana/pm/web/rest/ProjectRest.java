package com.faculana.pm.web.rest;

import com.faculana.pm.domain.Project;
import com.faculana.pm.service.ProjectService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/** CRUD mínimo de projetos. */
@RestController @RequestMapping("/api/projects")
public class ProjectRest {
  private final ProjectService svc;
  public ProjectRest(ProjectService svc){ this.svc = svc; }

  @GetMapping public List<Project> all(){ return svc.listar(); }
  @PostMapping public Project create(@Valid @RequestBody Project p){ return svc.criar(p); }
  @GetMapping("/{id}") public Project one(@PathVariable Long id){ return svc.obter(id); }
  @PutMapping("/{id}") public Project update(@PathVariable Long id, @Valid @RequestBody Project p){ return svc.atualizar(id,p); }
  @DeleteMapping("/{id}") public void delete(@PathVariable Long id){ svc.excluir(id); }
}
