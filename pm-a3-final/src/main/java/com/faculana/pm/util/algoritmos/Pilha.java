package com.faculana.pm.util.algoritmos;

import java.util.ArrayList;
import java.util.List;

/** Pilha genérica simples (LIFO). */
public class Pilha<T> {
  private final List<T> dados = new ArrayList<>();
  public void push(T v){ dados.add(v); }
  public T pop(){ return dados.isEmpty()? null : dados.remove(dados.size()-1); }
  public T peek(){ return dados.isEmpty()? null : dados.get(dados.size()-1); }
  public boolean isVazia(){ return dados.isEmpty(); }
  public int tamanho(){ return dados.size(); }
}
