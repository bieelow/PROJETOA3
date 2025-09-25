package com.gestaoprojetos.controllers;

import com.gestaoprojetos.repositories.EquipeRepository;
import com.gestaoprojetos.repositories.ProjetoRepository;
import com.gestaoprojetos.repositories.UsuarioRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    private final UsuarioRepository usuarioRepo;
    private final ProjetoRepository projetoRepo;
    private final EquipeRepository equipeRepo;

    public ViewController(UsuarioRepository usuarioRepo, ProjetoRepository projetoRepo, EquipeRepository equipeRepo) {
        this.usuarioRepo = usuarioRepo; this.projetoRepo = projetoRepo; this.equipeRepo = equipeRepo;
    }

    @GetMapping("/usuarios")
    public String listaUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepo.findAll());
        return "lista-usuarios";
    }

    @GetMapping("/projetos")
    public String listaProjetos(Model model) {
        model.addAttribute("projetos", projetoRepo.findAll());
        return "lista-projetos";
    }

    @GetMapping("/equipes")
    public String listaEquipes(Model model) {
        model.addAttribute("equipes", equipeRepo.findAll());
        return "lista-equipes";
    }
}
