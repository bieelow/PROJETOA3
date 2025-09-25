package com.faculana.pm.controller;

import com.faculana.pm.model.Team;
import com.faculana.pm.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamsController {
  private final TeamService service;

  public TeamsController(TeamService service) { this.service = service; }

  @GetMapping
  public List<Team> all() { return service.findAll(); }

  @PostMapping
  public Team create(@Valid @RequestBody Team t) { return service.save(t); }

  @PutMapping("/{id}")
  public ResponseEntity<Team> update(@PathVariable String id, @Valid @RequestBody Team t) {
    return service.findById(id)
      .map(ex -> { t.setId(id); return ResponseEntity.ok(service.save(t)); })
      .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
