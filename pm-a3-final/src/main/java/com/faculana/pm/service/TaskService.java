package com.faculana.pm.service;

import com.faculana.pm.domain.Task;
import com.faculana.pm.repository.TaskRepository;
import com.faculana.pm.web.NotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

/** Regras mínimas de Tarefa (CRUD). */
@Service
public class TaskService {
  private final TaskRepository repo;
  public TaskService(TaskRepository repo){ this.repo = repo; }

  public List<Task> listar(){ return repo.findAll(); }
  public Task criar(Task t){ return repo.save(t); }
  public Task obter(Long id){ return repo.findById(id).orElseThrow(() -> new NotFoundException("Tarefa não encontrada")); }
  public Task atualizar(Long id, Task t){
    var a = obter(id);
    a.setTitulo(t.getTitulo()); a.setDescricao(t.getDescricao());
    a.setPrioridade(t.getPrioridade()); a.setProjeto(t.getProjeto());
    return repo.save(a);
  }
  public void excluir(Long id){ repo.delete(obter(id)); }
}
