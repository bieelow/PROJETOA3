package com.faculana.pm;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
  @GetMapping("/")
  public String home(Model model) {
    model.addAttribute("appName", "Gestão de Projetos e Equipes");
    model.addAttribute("mensagem", "API no ar: /api/users, /api/teams, /api/projects");
    return "home";
  }
}
