package com.projeto.CadastroUsuario.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class UsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCadastrarUsuario() throws Exception{
        String usuarioJson = "{\"nome\":\"Eduardo\",\"email\":\"eduardo@gmail.com\",\"idade\":29}";

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioJson))
                .andExpect(status().isOk());
    }

    @Test
    void testBuscarUsuarioPorNome() throws Exception {
        String usuarioJson = "{\"nome\":\"Eduardo\",\"email\":\"eduardo@gmail.com\",\"idade\":29}";

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/customers/customer")
                .param("nome", "Eduardo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Eduardo"))
                .andExpect(jsonPath("$.email").value("eduardo@gmail.com"))
                .andExpect(jsonPath("$.idade").value(29));
    }

    @Test
    void testDeletarUsuario() throws Exception {
        String usuarioJson = "{\"nome\":\"Eduardo\",\"email\":\"eduardo@gmail.com\",\"idade\":29}";
        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJson))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/customers")
                        .param("nome", "Eduardo"))
                .andExpect(status().isOk());
    }
}