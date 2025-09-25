package com.faculana.pm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.faculana.pm.model.Carro;
import com.faculana.pm.model.Cliente;
import com.faculana.pm.repository.CarroRepository;
import com.faculana.pm.repository.ClienteRepository;

import java.util.List;

@Service
public class CarroService {

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public Carro salvarCarro(Carro carro) {
        if (carro.getCliente() != null && carro.getCliente().getId() != null) {
            Long cid = carro.getCliente().getId();
            Cliente cliente = clienteRepository.findById(cid)
                    .orElseThrow(() -> new IllegalArgumentException("Cliente not found with id: " + cid));
            carro.setCliente(cliente);
        }
        return carroRepository.save(carro);
    }

    public List<Carro> listarCarros() {
        return carroRepository.findAll();
    }
}
