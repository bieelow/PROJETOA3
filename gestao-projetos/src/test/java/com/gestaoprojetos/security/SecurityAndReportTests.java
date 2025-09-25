package com.gestaoprojetos.security;

import com.gestaoprojetos.models.Projeto;
import com.gestaoprojetos.models.Usuario;
import com.gestaoprojetos.repositories.ProjetoRepository;
import com.gestaoprojetos.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityAndReportTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private ProjetoRepository projetoRepo;

    private final BCryptPasswordEncoder enc = new BCryptPasswordEncoder();

    @BeforeEach
    void setup() {
        // apagar projetos antes de usuários para evitar violação de FK
        projetoRepo.deleteAll(); usuarioRepo.deleteAll();
        Usuario admin = new Usuario(); admin.setNome("Admin"); admin.setLogin("admin"); admin.setSenha(enc.encode("admin")); admin.setPerfil(Usuario.Perfil.ADMIN);
        Usuario col = new Usuario(); col.setNome("Col"); col.setLogin("col"); col.setSenha(enc.encode("col")); col.setPerfil(Usuario.Perfil.COLABORADOR);
        usuarioRepo.save(admin); usuarioRepo.save(col);
    }

    @Test
    void loginComUsuarioValido() throws Exception {
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf())
                .param("username","admin").param("password","admin"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void usuarioSemPermissaoNaoAcessaAreaRestrita() throws Exception {
        // col é COLABORADOR e não deve acessar /api/usuarios
        mockMvc.perform(get("/api/usuarios").with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic("col","col"))).andExpect(status().isForbidden());
    }

    @Test
    void relatorioRetornaProjetosPorStatus() throws Exception {
        Usuario g = usuarioRepo.findAll().stream().findFirst().orElse(null);
        Projeto p1 = new Projeto(); p1.setNome("P1"); p1.setStatus("EM ANDAMENTO"); p1.setDataInicio(LocalDate.now()); p1.setGerenteResponsavel(g);
        Projeto p2 = new Projeto(); p2.setNome("P2"); p2.setStatus("PLANEJADO"); p2.setDataInicio(LocalDate.now()); p2.setGerenteResponsavel(g);
        projetoRepo.save(p1); projetoRepo.save(p2);

        String html = mockMvc.perform(get("/relatorios").with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic("admin","admin"))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertThat(html).contains("EM ANDAMENTO");
        assertThat(html).contains("PLANEJADO");
    }
}
