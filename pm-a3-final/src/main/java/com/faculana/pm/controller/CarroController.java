package com.faculana.pm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.faculana.pm.model.Carro;
import com.faculana.pm.service.CarroService;

import java.util.List;

@RestController
@RequestMapping("/api/carros")
public class CarroController {

    @Autowired
    private CarroService carroService;

    @PostMapping
    public Carro salvarCarro(@RequestBody Carro carro) {
        return carroService.salvarCarro(carro);
    }

    @GetMapping
    public List<Carro> listarCarros() {
        return carroService.listarCarros();
    }
}
