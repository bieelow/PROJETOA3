package com.faculana.pm.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

/** Time com conjunto mínimo de membros. */
@Entity
public class Team {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotBlank private String nome;

  @ManyToMany
  private Set<User> membros;

  public Long getId(){ return id; }
  public String getNome(){ return nome; }
  public void setNome(String nome){ this.nome = nome; }
  public Set<User> getMembros(){ return membros; }
  public void setMembros(Set<User> membros){ this.membros = membros; }
}
