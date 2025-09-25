package com.faculana.pm.util.algoritmos;

import java.util.ArrayDeque;
import java.util.Queue;

/** Fila genérica simples (FIFO). */
public class Fila<T> {
  private final Queue<T> q = new ArrayDeque<>();
  public void enqueue(T v){ q.add(v); }
  public T dequeue(){ return q.poll(); }
  public boolean isVazia(){ return q.isEmpty(); }
  public int tamanho(){ return q.size(); }
}
