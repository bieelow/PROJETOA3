package com.faculana.pm.service;

import com.faculana.pm.model.Role;
import com.faculana.pm.model.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
  private final Map<String, User> store = new LinkedHashMap<>();

  public List<User> findAll() {
    return new ArrayList<>(store.values());
  }

  public Optional<User> findById(String id) {
    return Optional.ofNullable(store.get(id));
  }

  public User save(User u) {
    if (u.getId() == null || u.getId().isBlank()) {
      u.setId(UUID.randomUUID().toString());
    }
    store.put(u.getId(), u);
    return u;
  }

  public void delete(String id) {
    store.remove(id);
  }

  // Popular 1x (opcional)
  public void seedIfEmpty() {
    if (store.isEmpty()) {
      User a = new User();
      a.setName("Ana");
      a.setEmail("ana@empresa.com");
      a.setRole(Role.COLABORADOR);
      save(a);

      User b = new User();
      b.setName("Beatriz");
      b.setEmail("bea@empresa.com");
      b.setRole(Role.GERENTE);
      save(b);
    }
  }
}
