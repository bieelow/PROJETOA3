package com.faculana.pm.util.algoritmos;

import org.springframework.stereotype.Component;

/** Armazena pilha e fila na memória para os endpoints de demonstração. */
@Component
public class DemoStore {
  public final Pilha<String> pilha = new Pilha<>();
  public final Fila<String> fila = new Fila<>();
}
