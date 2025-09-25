package com.faculana.pm.service;

import com.faculana.pm.model.Project;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    private List<Project> projects = new ArrayList<>();

    public List<Project> findAll() {
        return projects;  // Retorna todos os projetos cadastrados
    }

    public Project save(Project p) {
        projects.add(p);  // Adiciona um novo projeto na lista
        return p;  // Retorna o projeto salvo
    }
}
