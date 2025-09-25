package com.faculana.pm.web;
/** 404 simples quando o recurso não existe. */
public class NotFoundException extends RuntimeException {
  public NotFoundException(String msg){ super(msg); }
}
