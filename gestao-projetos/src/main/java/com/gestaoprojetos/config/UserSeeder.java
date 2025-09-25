package com.gestaoprojetos.config;

import com.gestaoprojetos.models.Usuario;
import com.gestaoprojetos.models.Usuario.Perfil;
import com.gestaoprojetos.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserSeeder {
    @Bean
    public CommandLineRunner seedUsers(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.count() == 0) {
                Usuario ana = new Usuario();
                ana.setNome("Ana Silva");
                ana.setCpf("11111111111");
                ana.setEmail("ana@email.com");
                ana.setCargo("Gerente de Projetos");
                ana.setLogin("ana");
                ana.setSenha(encoder.encode("123"));
                ana.setPerfil(Perfil.GERENTE);

                Usuario gabriel = new Usuario();
                gabriel.setNome("Gabriel Santos");
                gabriel.setCpf("22222222222");
                gabriel.setEmail("gabriel@email.com");
                gabriel.setCargo("Colaborador");
                gabriel.setLogin("gabriel");
                gabriel.setSenha(encoder.encode("123"));
                gabriel.setPerfil(Perfil.COLABORADOR);

                Usuario leonardo = new Usuario();
                leonardo.setNome("Leonardo Telves");
                leonardo.setCpf("33333333333");
                leonardo.setEmail("leonardo@email.com");
                leonardo.setCargo("Administrador do Sistema");
                leonardo.setLogin("leonardo");
                leonardo.setSenha(encoder.encode("123"));
                leonardo.setPerfil(Perfil.ADMIN);

                repo.save(ana);
                repo.save(gabriel);
                repo.save(leonardo);
            }
        };
    }
}