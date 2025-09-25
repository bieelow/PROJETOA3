package com.faculana.pm.util.algoritmos;

import java.util.List;

/** Busca binária case-insensitive. Retorna índice ou -1. */
public class Busca {
  public static int binarySearch(List<String> ordenada, String alvo){
    int l=0, r=ordenada.size()-1;
    while(l<=r){
      int m=(l+r)/2;
      int cmp=ordenada.get(m).compareToIgnoreCase(alvo);
      if(cmp==0) return m;
      if(cmp<0) l=m+1; else r=m-1;
    }
    return -1;
  }
}
