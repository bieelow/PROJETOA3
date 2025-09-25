package com.gestaoprojetos.controllers;

import com.gestaoprojetos.models.Projeto;
import com.gestaoprojetos.models.Usuario;
import com.gestaoprojetos.repositories.ProjetoRepository;
import com.gestaoprojetos.repositories.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projetos")
public class ProjetoController {

    private final ProjetoRepository repo;
    private final UsuarioRepository usuarioRepo;

    public ProjetoController(ProjetoRepository repo, UsuarioRepository usuarioRepo) {
        this.repo = repo; this.usuarioRepo = usuarioRepo;
    }

    @GetMapping
    public List<Projeto> listar() { return repo.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Projeto> buscar(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Projeto> criar(@RequestBody Projeto projeto) {
        if (projeto.getGerenteResponsavel() != null && projeto.getGerenteResponsavel().getId() != null) {
            Usuario g = usuarioRepo.findById(projeto.getGerenteResponsavel().getId()).orElse(null);
            projeto.setGerenteResponsavel(g);
        }
        Projeto salvo = repo.save(projeto);
        return ResponseEntity.ok(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Projeto> atualizar(@PathVariable Long id, @RequestBody Projeto dados) {
        return repo.findById(id).map(p -> {
            p.setNome(dados.getNome()); p.setDescricao(dados.getDescricao());
            p.setDataInicio(dados.getDataInicio()); p.setDataFimPrevista(dados.getDataFimPrevista());
            p.setStatus(dados.getStatus());
            if (dados.getGerenteResponsavel() != null && dados.getGerenteResponsavel().getId() != null) {
                p.setGerenteResponsavel(usuarioRepo.findById(dados.getGerenteResponsavel().getId()).orElse(null));
            }
            return ResponseEntity.ok(repo.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return repo.findById(id).map(p -> { repo.delete(p); return ResponseEntity.noContent().<Void>build(); }).orElse(ResponseEntity.notFound().build());
    }
}
