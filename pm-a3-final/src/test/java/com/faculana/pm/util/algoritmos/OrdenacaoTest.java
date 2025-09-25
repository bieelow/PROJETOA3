package com.faculana.pm.util.algoritmos;

import org.junit.jupiter.api.Test;
import java.util.Comparator;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

// Testa que o mergeSort ordena uma lista de inteiros em ordem crescente.
class OrdenacaoTest {
  @Test
  void mergeSort_ordenaInteirosCrescente() {
    var entrada = List.of(5, 1, 3, 2, 4);
    var saida = Ordenacao.mergeSort(entrada, Comparator.naturalOrder());
    assertEquals(List.of(1,2,3,4,5), saida, "deve ordenar em ordem crescente");
  }
}
