package com.faculana.pm.web.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller simples para servir páginas Thymeleaf mínimas.
 */
@Controller
public class HomeController {

  @GetMapping("/")
  public String home() { return "home"; }       // templates/home.html
}
