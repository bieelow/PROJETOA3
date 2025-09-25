package com.faculana.pm.controller;

import com.faculana.pm.model.User;
import com.faculana.pm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
    this.service.seedIfEmpty(); // cria 2 usuários de exemplo
  }

  @GetMapping
  public List<User> all() { return service.findAll(); }

  @PostMapping
  public User create(@Valid @RequestBody User u) { return service.save(u); }

  @PutMapping("/{id}")
  public ResponseEntity<User> update(@PathVariable String id, @Valid @RequestBody User u) {
    return service.findById(id)
      .map(ex -> { u.setId(id); return ResponseEntity.ok(service.save(u)); })
      .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
