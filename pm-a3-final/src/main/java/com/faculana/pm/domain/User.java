package com.faculana.pm.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/** Usuário simples do sistema (mínimo viável). */
@Entity
@Table(name = "app_user")
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotBlank private String nome;
  @Email @Column(unique = true) private String email;

  public Long getId(){ return id; }
  public String getNome(){ return nome; }
  public void setNome(String nome){ this.nome = nome; }
  public String getEmail(){ return email; }
  public void setEmail(String email){ this.email = email; }
}
