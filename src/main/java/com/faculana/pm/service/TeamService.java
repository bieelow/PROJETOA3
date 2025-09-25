package com.faculana.pm.service;

import com.faculana.pm.model.Team;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TeamService {
  private final Map<String, Team> store = new LinkedHashMap<>();

  public List<Team> findAll() {
    return new ArrayList<>(store.values());
  }

  public Optional<Team> findById(String id) {
    return Optional.ofNullable(store.get(id));
  }

  public Team save(Team t) {
    if (t.getId() == null || t.getId().isBlank()) {
      t.setId(UUID.randomUUID().toString());
    }
    store.put(t.getId(), t);
    return t;
  }

  public void delete(String id) {
    store.remove(id);
  }
}
