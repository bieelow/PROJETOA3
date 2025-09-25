package com.faculana.pm.util.algoritmos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Teste simples para garantir comportamento FIFO da fila.
class FilaTest {
  @Test
  void enqueue_dequeue_funciona() {
    var f = new Fila<String>(); f.enqueue("A"); f.enqueue("B");
    assertEquals("A", f.dequeue()); assertEquals("B", f.dequeue()); assertNull(f.dequeue());
  }
}
