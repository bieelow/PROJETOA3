package com.faculana.pm.web.rest;

import com.faculana.pm.domain.Task;
import com.faculana.pm.service.TaskService;
import com.faculana.pm.util.algoritmos.Busca;
import com.faculana.pm.util.algoritmos.Ordenacao;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Comparator;
import java.util.List;

/** CRUD de tarefas + endpoints que demonstram algoritmos. */
@RestController @RequestMapping("/api/tasks")
public class TaskRest {
  private final TaskService svc;
  public TaskRest(TaskService svc){ this.svc = svc; }

  @GetMapping public List<Task> all(){ return svc.listar(); }
  @PostMapping public Task create(@Valid @RequestBody Task t){ return svc.criar(t); }

  /** Ordena por prioridade (1..3) usando merge sort genérico. */
  @GetMapping("/sort")
  public List<Task> sortByPriority(){
    var lista = svc.listar();
    return Ordenacao.mergeSort(lista, Comparator.comparing(Task::getPrioridade));
  }

  /** Busca binária por título (case-insensitive) após ordenar alfabeticamente. */
  @GetMapping("/search")
  public Object searchByTitle(@RequestParam String q){
    var titulos = svc.listar().stream().map(Task::getTitulo).sorted(String::compareToIgnoreCase).toList();
    int idx = Busca.binarySearch(titulos, q);
    return idx >= 0 ? titulos.get(idx) : "não encontrado";
  }
}
