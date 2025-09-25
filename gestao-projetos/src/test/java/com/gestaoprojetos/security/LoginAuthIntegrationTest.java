package com.gestaoprojetos.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginAuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void seededUsersExistAndCanLogin() throws Exception {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT login, nome, senha FROM usuario");
        // print found users for visibility
        System.out.println("Usuarios encontrados no banco:");
        rows.forEach(r -> System.out.println(r.get("login") + " - " + r.get("nome") + " - senhaHash=" + r.get("senha")));

        // ensure the three expected logins exist
        List<String> logins = rows.stream().map(r -> (String) r.get("login")).toList();
        assertThat(logins).contains("ana", "gabriel", "leonardo");

        // Try form-login for each user and verify we can access /teste using the session cookie
        for (String login : List.of("ana", "gabriel", "leonardo")) {
        // perform form login using Spring Security test support
        MvcResult res = mockMvc.perform(formLogin().user(login).password("123"))
            .andReturn();

        int status = res.getResponse().getStatus();
        HttpSession session = res.getRequest().getSession(false);
        System.out.println("Login attempt for '" + login + "' returned status=" + status + " session=" + session);

        assertThat(status).withFailMessage("Login returned forbidden for " + login).isNotEqualTo(403);
        assertThat(session).withFailMessage("No session created for login=" + login).isNotNull();

        // Use the authenticated principal to access protected endpoint (mock user from session)
        mockMvc.perform(get("/teste").sessionAttr("SPRING_SECURITY_CONTEXT", res.getRequest().getSession().getAttribute("SPRING_SECURITY_CONTEXT")))
            .andExpect(status().isOk())
            .andExpect(content().string("API funcionando"));
        }
    }
}
