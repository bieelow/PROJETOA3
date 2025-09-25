package com.faculana.pm.util.algoritmos;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

// Testa a busca binária em listas de strings ordenadas.
class BuscaTest {
  @Test
  void binarySearch_encontraElemento() {
    var lista = List.of("Alpha","Beta","Gamma","Omega"); // já ordenada
    assertEquals(1, Busca.binarySearch(lista, "Beta"));
  }

  @Test
  void binarySearch_naoEncontra() {
    var lista = List.of("Alpha","Beta","Gamma","Omega");
    assertEquals(-1, Busca.binarySearch(lista, "Delta"));
  }
}
