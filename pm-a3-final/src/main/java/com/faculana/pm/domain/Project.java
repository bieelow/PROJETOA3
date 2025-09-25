package com.faculana.pm.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/** Projeto pertence a um time e possui status. */
@Entity
public class Project {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotBlank private String titulo;

  @Enumerated(EnumType.STRING)
  private ProjectStatus status = ProjectStatus.PLANEJADO;

  @ManyToOne
  private Team time;

  public Long getId(){ return id; }
  public String getTitulo(){ return titulo; }
  public void setTitulo(String titulo){ this.titulo = titulo; }
  public ProjectStatus getStatus(){ return status; }
  public void setStatus(ProjectStatus status){ this.status = status; }
  public Team getTime(){ return time; }
  public void setTime(Team time){ this.time = time; }
}
