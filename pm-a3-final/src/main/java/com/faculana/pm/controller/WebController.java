package com.faculana.pm.controller;

import com.faculana.pm.model.Cliente;
import com.faculana.pm.model.Carro;
import com.faculana.pm.service.ClienteService;
import com.faculana.pm.service.CarroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    private final ClienteService clienteService;
    private final CarroService carroService;

    public WebController(ClienteService clienteService, CarroService carroService) {
        this.clienteService = clienteService;
        this.carroService = carroService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/clientes")
    public String clienteForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes";
    }

    @GetMapping("/carros")
    public String carroForm(Model model) {
        model.addAttribute("carro", new Carro());
        model.addAttribute("clientes", clienteService.listarClientes());
        return "carros";
    }

    @GetMapping("/clientes/listar")
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.listarClientes());
        return "listarClientes";
    }

    @GetMapping("/carros/listar")
    public String listarCarros(Model model) {
        model.addAttribute("carros", carroService.listarCarros());
        return "listarCarros";
    }
}
package com.faculana.pm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.faculana.pm.model.Cliente;
import com.faculana.pm.model.Carro;
import com.faculana.pm.service.ClienteService;
import com.faculana.pm.service.CarroService;

@Controller
public class WebController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CarroService carroService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/clientes")
    public String clientesForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes";
    }

    @PostMapping("/clientes")
    public String salvarCliente(@ModelAttribute Cliente cliente) {
        clienteService.salvarCliente(cliente);
        return "redirect:/clientes/listar";
    }

    @GetMapping("/clientes/listar")
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.listarClientes());
        return "listarClientes";
    }

    @GetMapping("/carros")
    public String carrosForm(Model model) {
        model.addAttribute("carro", new Carro());
        model.addAttribute("clientes", clienteService.listarClientes());
        return "carros";
    }

    @PostMapping("/carros")
    public String salvarCarro(@ModelAttribute Carro carro) {
        carroService.salvarCarro(carro);
        return "redirect:/carros/listar";
    }

    @GetMapping("/carros/listar")
    public String listarCarros(Model model) {
        model.addAttribute("carros", carroService.listarCarros());
        return "listarCarros";
    }
}
