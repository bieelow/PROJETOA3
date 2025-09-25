package com.gestaoprojetos.controllers;

import com.gestaoprojetos.models.Projeto;
import com.gestaoprojetos.repositories.ProjetoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class RelatorioController {

    private final ProjetoRepository projetoRepo;

    public RelatorioController(ProjetoRepository projetoRepo) { this.projetoRepo = projetoRepo; }

    @GetMapping("/relatorios")
    public String relatorios(Model model) {
        List<Projeto> projetos = projetoRepo.findAll();
        Map<String, List<Projeto>> agrupado = projetos.stream().collect(Collectors.groupingBy(p -> p.getStatus() == null ? "SEM STATUS" : p.getStatus()));

        // garantir os grupos principais
        Map<String, List<Projeto>> porStatus = new java.util.LinkedHashMap<>();
        porStatus.put("PLANEJADO", agrupado.getOrDefault("PLANEJADO", java.util.Collections.emptyList()));
        porStatus.put("EM ANDAMENTO", agrupado.getOrDefault("EM ANDAMENTO", java.util.Collections.emptyList()));
        porStatus.put("CONCLUÍDO", agrupado.getOrDefault("CONCLUÍDO", java.util.Collections.emptyList()));
        porStatus.put("CANCELADO", agrupado.getOrDefault("CANCELADO", java.util.Collections.emptyList()));

        // colocar outros status (se existirem) embaixo
        agrupado.entrySet().stream()
                .filter(e -> !porStatus.containsKey(e.getKey()))
                .forEach(e -> porStatus.put(e.getKey(), e.getValue()));

        model.addAttribute("porStatus", porStatus);
        return "relatorios";
    }
}
