package com.faculana.pm.service;

import com.faculana.pm.domain.Project;
import com.faculana.pm.repository.ProjectRepository;
import com.faculana.pm.web.NotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

/** Regras mínimas de Projeto (CRUD). */
@Service
public class ProjectService {
  private final ProjectRepository repo;
  public ProjectService(ProjectRepository repo){ this.repo = repo; }

  public List<Project> listar(){ return repo.findAll(); }
  public Project criar(Project p){ return repo.save(p); }
  public Project obter(Long id){ return repo.findById(id).orElseThrow(() -> new NotFoundException("Projeto não encontrado")); }
  public Project atualizar(Long id, Project p){
    var a = obter(id);
    a.setTitulo(p.getTitulo()); a.setStatus(p.getStatus()); a.setTime(p.getTime());
    return repo.save(a);
  }
  public void excluir(Long id){ repo.delete(obter(id)); }
}
