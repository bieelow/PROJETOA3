package com.faculana.pm.web.mvc;

import com.faculana.pm.domain.Project;
import com.faculana.pm.repository.ProjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProjectsController {
  private final ProjectRepository repo;
  public ProjectsController(ProjectRepository repo){ this.repo = repo; }

  @GetMapping("/projects")
  public String list(Model model){
    model.addAttribute("projects", repo.findAll());
    model.addAttribute("novo", new Project());
    return "projects";
  }

  @PostMapping("/projects")
  public String add(@ModelAttribute("novo") Project p){
    repo.save(p);
    return "redirect:/projects";
  }
}
