package com.imckify.bakis.endpoints.api;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String api = "/api/Users";

//    https://spring.io/guides/gs/testing-web/

    @Test
    public void greetingShouldReturnDefaultMessage2() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + api, String.class)).contains("investor");
    }
}