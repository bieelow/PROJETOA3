package com.gestaoprojetos.security;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityPermissionsAndReportTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private ProjetoRepository projetoRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario admin, gerente, colaborador;

    @BeforeEach
    void setup() {
        // limpar projetos antes de usuários para evitar FK
        projetoRepo.deleteAll();
        usuarioRepo.deleteAll();

        admin = new Usuario(); admin.setNome("Admin"); admin.setLogin("admin"); admin.setSenha(encoder.encode("admin")); admin.setPerfil(Usuario.Perfil.ADMIN);
        gerente = new Usuario(); gerente.setNome("Gerente"); gerente.setLogin("gerente"); gerente.setSenha(encoder.encode("gerente")); gerente.setPerfil(Usuario.Perfil.GERENTE);
        colaborador = new Usuario(); colaborador.setNome("Colab"); colaborador.setLogin("colab"); colaborador.setSenha(encoder.encode("colab")); colaborador.setPerfil(Usuario.Perfil.COLABORADOR);

        usuarioRepo.save(admin); usuarioRepo.save(gerente); usuarioRepo.save(colaborador);
    }

    @Test
    void teste_login_com_usuario_admin() throws Exception {
        mockMvc.perform(post("/login").with(csrf()).param("username","admin").param("password","admin")).andExpect(status().is3xxRedirection());
    }

    @Test
    void teste_colaborador_get_e_nao_post_projetos() throws Exception {
        // GET deve funcionar
        mockMvc.perform(get("/api/projetos").with(httpBasic("colab","colab"))).andExpect(status().isOk());

        // POST deve ser proibido para colaborador
        Projeto novo = new Projeto(); novo.setNome("Proj Colab"); novo.setStatus("PLANEJADO");
        String json = objectMapper.writeValueAsString(novo);
        mockMvc.perform(post("/api/projetos").with(httpBasic("colab","colab")).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isForbidden());
    }

    @Test
    void teste_gerente_cria_e_atualiza_projeto() throws Exception {
        // gerente cria projeto
        Projeto novo = new Projeto(); novo.setNome("Proj G"); novo.setStatus("PLANEJADO"); novo.setGerenteResponsavel(gerente);
        String json = objectMapper.writeValueAsString(novo);
        String retorno = mockMvc.perform(post("/api/projetos").with(httpBasic("gerente","gerente")).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Projeto criado = objectMapper.readValue(retorno, Projeto.class);
        assertThat(criado.getId()).isNotNull();
        assertThat(criado.getNome()).isEqualTo("Proj G");

        // atualizar
        criado.setNome("Proj G Atualizado");
        String upd = objectMapper.writeValueAsString(criado);
        mockMvc.perform(put("/api/projetos/" + criado.getId()).with(httpBasic("gerente","gerente")).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(upd)).andExpect(status().isOk());
    }

    @Test
    void teste_admin_acessa_api_usuarios() throws Exception {
        mockMvc.perform(get("/api/usuarios").with(httpBasic("admin","admin"))).andExpect(status().isOk());
    }

    @Test
    void teste_relatorio_agrupa_projetos_por_status() throws Exception {
        Projeto p1 = new Projeto(); p1.setNome("P-Planejado"); p1.setStatus("PLANEJADO"); p1.setDataInicio(LocalDate.now()); p1.setGerenteResponsavel(gerente);
        Projeto p2 = new Projeto(); p2.setNome("P-Andamento"); p2.setStatus("EM ANDAMENTO"); p2.setDataInicio(LocalDate.now()); p2.setGerenteResponsavel(gerente);
        Projeto p3 = new Projeto(); p3.setNome("P-Concluido"); p3.setStatus("CONCLUÍDO"); p3.setDataInicio(LocalDate.now()); p3.setGerenteResponsavel(gerente);
        projetoRepo.save(p1); projetoRepo.save(p2); projetoRepo.save(p3);

        String html = mockMvc.perform(get("/relatorios").with(httpBasic("admin","admin"))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertThat(html).contains("PLANEJADO");
        assertThat(html).contains("EM ANDAMENTO");
        assertThat(html).contains("CONCLUÍDO");
        assertThat(html).contains("P-Planejado");
        assertThat(html).contains("P-Andamento");
        assertThat(html).contains("P-Concluido");
    }
}
