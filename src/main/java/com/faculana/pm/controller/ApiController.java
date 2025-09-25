package com.faculana.pm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api") // rota base
public class ApiController {

    @GetMapping("/info") // acessível em: http://localhost:8080/api/info
    public String info() {
        return "API funcionando";
    }
}
