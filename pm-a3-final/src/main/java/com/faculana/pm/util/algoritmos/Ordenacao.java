package com.faculana.pm.util.algoritmos;

import java.util.*;

public class Ordenacao {
  /** Merge sort genérico: estável e O(n log n). */
  public static <T> List<T> mergeSort(List<T> lista, Comparator<T> comp){
    if (lista.size() <= 1) return new ArrayList<>(lista);
    int mid = lista.size()/2;
    var esq = mergeSort(lista.subList(0, mid), comp);
    var dir = mergeSort(lista.subList(mid, lista.size()), comp);
    return mesclar(esq, dir, comp);
  }
  private static <T> List<T> mesclar(List<T> a, List<T> b, Comparator<T> comp){
    var r = new ArrayList<T>(a.size()+b.size());
    int i=0, j=0;
    while(i<a.size() || j<b.size()){
      if (j==b.size() || (i<a.size() && comp.compare(a.get(i), b.get(j))<=0)) r.add(a.get(i++));
      else r.add(b.get(j++));
    }
    return r;
  }
}
