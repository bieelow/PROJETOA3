package com.gestaoprojetos.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TesteControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testeEndpointRetornaApiFuncionando() {
        String base = "http://localhost:" + port + "/api/teste";
        String resp = this.restTemplate.getForObject(base, String.class);
        assertThat(resp).isEqualTo("API funcionando");
    }
}
