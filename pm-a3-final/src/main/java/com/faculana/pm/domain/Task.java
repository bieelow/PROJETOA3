package com.faculana.pm.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/** Tarefa essencial: título, prioridade, vínculo ao projeto. */
@Entity
public class Task {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotBlank private String titulo;
  private String descricao;
  private Integer prioridade = 1; // 1=baixa; 2=média; 3=alta

  @ManyToOne(optional = false)
  private Project projeto;

  private LocalDateTime criadaEm = LocalDateTime.now();

  public Long getId(){ return id; }
  public String getTitulo(){ return titulo; }
  public void setTitulo(String titulo){ this.titulo = titulo; }
  public String getDescricao(){ return descricao; }
  public void setDescricao(String descricao){ this.descricao = descricao; }
  public Integer getPrioridade(){ return prioridade; }
  public void setPrioridade(Integer prioridade){ this.prioridade = prioridade; }
  public Project getProjeto(){ return projeto; }
  public void setProjeto(Project projeto){ this.projeto = projeto; }
  public LocalDateTime getCriadaEm(){ return criadaEm; }
}
