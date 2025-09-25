package com.faculana.pm.web.rest;

import com.faculana.pm.util.algoritmos.DemoStore;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/** Endpoints mínimos para provar Pilha e Fila. */
@RestController @RequestMapping("/api/demo")
public class DemoEstruturasRest {
  private final DemoStore store;
  public DemoEstruturasRest(DemoStore store){ this.store = store; }

  @PostMapping("/stack/push") public Map<String,Object> push(@RequestParam String v){ store.pilha.push(v); return Map.of("topo", store.pilha.peek(), "tamanho", store.pilha.tamanho()); }
  @PostMapping("/stack/pop")  public Map<String,Object> pop(){ var x = store.pilha.pop(); return Map.of("valor", x, "tamanho", store.pilha.tamanho()); }

  @PostMapping("/queue/enqueue") public Map<String,Object> enq(@RequestParam String v){ store.fila.enqueue(v); return Map.of("tamanho", store.fila.tamanho()); }
  @PostMapping("/queue/dequeue") public Map<String,Object> deq(){ var x = store.fila.dequeue(); return Map.of("valor", x, "tamanho", store.fila.tamanho()); }
}
