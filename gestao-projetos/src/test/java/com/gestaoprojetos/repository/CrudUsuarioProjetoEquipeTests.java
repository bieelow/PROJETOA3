package com.gestaoprojetos.repository;

import com.gestaoprojetos.models.Equipe;
import com.gestaoprojetos.models.Projeto;
import com.gestaoprojetos.models.Usuario;
import com.gestaoprojetos.repositories.EquipeRepository;
import com.gestaoprojetos.repositories.ProjetoRepository;
import com.gestaoprojetos.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CrudUsuarioProjetoEquipeTests {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private ProjetoRepository projetoRepo;

    @Autowired
    private EquipeRepository equipeRepo;

    @Test
    void salvarEBuscarUsuario() {
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder enc = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        Usuario u = new Usuario();
        u.setNome("João"); u.setCpf("12345678900"); u.setEmail("joao@teste.com"); u.setCargo("DEV");
        u.setLogin("joao"); u.setSenha(enc.encode("123")); u.setPerfil(Usuario.Perfil.COLABORADOR);
        Usuario salvo = usuarioRepo.save(u);
        assertThat(salvo.getId()).isNotNull();
        Usuario buscado = usuarioRepo.findById(salvo.getId()).orElse(null);
        assertThat(buscado).isNotNull();
        assertThat(buscado.getNome()).isEqualTo("João");
    }

    @Test
    void salvarProjetoComGerente() {
    org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder enc = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    Usuario gerente = new Usuario(); gerente.setNome("Maria"); gerente.setCpf("22233344455"); gerente.setEmail("maria@t.com"); gerente.setCargo("Gerente"); gerente.setLogin("maria"); gerente.setSenha(enc.encode("abc")); gerente.setPerfil(Usuario.Perfil.GERENTE);
        gerente = usuarioRepo.save(gerente);

        Projeto p = new Projeto(); p.setNome("Projeto X"); p.setDescricao("Descricao X"); p.setDataInicio(LocalDate.now()); p.setDataFimPrevista(LocalDate.now().plusDays(30));
        p.setStatus("ATIVO"); p.setGerenteResponsavel(gerente);
        Projeto salvo = projetoRepo.save(p);
        assertThat(salvo.getId()).isNotNull();
        Projeto buscado = projetoRepo.findById(salvo.getId()).orElse(null);
        assertThat(buscado).isNotNull();
        assertThat(buscado.getGerenteResponsavel()).isNotNull();
        assertThat(buscado.getGerenteResponsavel().getId()).isEqualTo(gerente.getId());
    }

    @Test
    void salvarEquipeComMembros() {
    org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder enc2 = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    Usuario u1 = new Usuario(); u1.setNome("Ana"); u1.setCpf("11122233344"); u1.setEmail("ana@t.com"); u1.setCargo("QA"); u1.setLogin("ana"); u1.setSenha(enc2.encode("pw")); u1.setPerfil(Usuario.Perfil.COLABORADOR);
    Usuario u2 = new Usuario(); u2.setNome("Pedro"); u2.setCpf("55566677788"); u2.setEmail("pedro@t.com"); u2.setCargo("DEV"); u2.setLogin("pedro"); u2.setSenha(enc2.encode("pw")); u2.setPerfil(Usuario.Perfil.COLABORADOR);
        u1 = usuarioRepo.save(u1); u2 = usuarioRepo.save(u2);

        Equipe e = new Equipe(); e.setNome("Equipe A"); e.setDescricao("Equipe de teste");
        e.getMembros().add(u1); e.getMembros().add(u2);
        Equipe salvo = equipeRepo.save(e);
        assertThat(salvo.getId()).isNotNull();
        Equipe buscado = equipeRepo.findById(salvo.getId()).orElse(null);
        assertThat(buscado).isNotNull();
        assertThat(buscado.getMembros()).hasSize(2);
    }
}
