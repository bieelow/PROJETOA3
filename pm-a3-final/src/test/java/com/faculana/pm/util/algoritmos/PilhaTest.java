package com.faculana.pm.util.algoritmos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Teste simples para garantir comportamento LIFO da pilha.
class PilhaTest {
  @Test
  void push_pop_funciona() {
    var p = new Pilha<String>(); p.push("A"); p.push("B");
    assertEquals("B", p.pop()); assertEquals("A", p.pop()); assertNull(p.pop());
  }
}
