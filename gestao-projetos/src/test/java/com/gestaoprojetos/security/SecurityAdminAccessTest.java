package com.gestaoprojetos.security;

import com.gestaoprojetos.models.Usuario;
import com.gestaoprojetos.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityAdminAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepo;

    private final BCryptPasswordEncoder enc = new BCryptPasswordEncoder();

    @BeforeEach
    void setup() {
        usuarioRepo.deleteAll();
        Usuario admin = new Usuario();
        admin.setNome("Admin");
        admin.setLogin("admin");
        admin.setSenha(enc.encode("admin"));
        admin.setPerfil(Usuario.Perfil.ADMIN);
        usuarioRepo.save(admin);
    }

    @Test
    void adminAcessaApiUsuarios() throws Exception {
        mockMvc.perform(get("/api/usuarios").with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic("admin","admin"))).andExpect(status().isOk());
    }
}
