package com.gestaoprojetos.controllers;

import com.gestaoprojetos.models.Equipe;
import com.gestaoprojetos.models.Projeto;
import com.gestaoprojetos.models.Usuario;
import com.gestaoprojetos.repositories.EquipeRepository;
import com.gestaoprojetos.repositories.ProjetoRepository;
import com.gestaoprojetos.repositories.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/equipes")
public class EquipeController {

    private final EquipeRepository repo;
    private final UsuarioRepository usuarioRepo;
    private final ProjetoRepository projetoRepo;

    public EquipeController(EquipeRepository repo, UsuarioRepository usuarioRepo, ProjetoRepository projetoRepo) {
        this.repo = repo; this.usuarioRepo = usuarioRepo; this.projetoRepo = projetoRepo;
    }

    @GetMapping
    public List<Equipe> listar() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Equipe> buscar(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Equipe criar(@RequestBody Equipe equipe) {
        // se vier ids de membros ou projetos, associar
        if (equipe.getMembros() != null) {
            Set<Usuario> membros = equipe.getMembros().stream().map(u -> usuarioRepo.findById(u.getId()).orElse(null)).filter(u -> u != null).collect(Collectors.toSet());
            equipe.setMembros(membros);
        }
        if (equipe.getProjetos() != null) {
            Set<Projeto> projetos = equipe.getProjetos().stream().map(p -> projetoRepo.findById(p.getId()).orElse(null)).filter(p -> p != null).collect(Collectors.toSet());
            equipe.setProjetos(projetos);
        }
        return repo.save(equipe);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Equipe> atualizar(@PathVariable Long id, @RequestBody Equipe dados) {
        return repo.findById(id).map(e -> {
            e.setNome(dados.getNome()); e.setDescricao(dados.getDescricao());
            if (dados.getMembros() != null) {
                Set<Usuario> membros = dados.getMembros().stream().map(u -> usuarioRepo.findById(u.getId()).orElse(null)).filter(u2 -> u2 != null).collect(Collectors.toSet());
                e.setMembros(membros);
            }
            if (dados.getProjetos() != null) {
                Set<Projeto> projetos = dados.getProjetos().stream().map(p -> projetoRepo.findById(p.getId()).orElse(null)).filter(p2 -> p2 != null).collect(Collectors.toSet());
                e.setProjetos(projetos);
            }
            return ResponseEntity.ok(repo.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return repo.findById(id).map(e -> { repo.delete(e); return ResponseEntity.noContent().<Void>build(); }).orElse(ResponseEntity.notFound().build());
    }
}
