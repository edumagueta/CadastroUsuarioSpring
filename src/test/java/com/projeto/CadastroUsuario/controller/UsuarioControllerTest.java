package com.projeto.CadastroUsuario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.CadastroUsuario.model.Usuario;
import com.projeto.CadastroUsuario.dto.UsuarioRequest;
import com.projeto.CadastroUsuario.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
    }

    @Test
    void deveCadastrarUsuario() throws Exception {
        Usuario usuario = criarUsuario("Eduardo", "eduardo@gmail.com", "12345678900");
        when(usuarioService.salvarUsuario(any(UsuarioRequest.class))).thenReturn(usuario);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UsuarioRequest("Eduardo", "eduardo@gmail.com", null))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Eduardo"))
                .andExpect(jsonPath("$.email").value("eduardo@gmail.com"))
                .andExpect(jsonPath("$.dataNascimento").doesNotExist())
                .andExpect(jsonPath("$.documento").doesNotExist());
    }

    @Test
    void deveBuscarUsuarioPorNome() throws Exception {
        Usuario usuario = criarUsuario("Eduardo", "eduardo@gmail.com", "12345678900");
        when(usuarioService.buscarUsuariosPorNome("Eduardo")).thenReturn(List.of(usuario));

        mockMvc.perform(get("/customers")
                        .param("nome", "Eduardo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Eduardo"))
                .andExpect(jsonPath("$[0].email").value("eduardo@gmail.com"));
    }

    @Test
    void deveDeletarUsuarioPorEmail() throws Exception {
        when(usuarioService.deletarUsuarioPorEmail("eduardo@gmail.com")).thenReturn(true);

        mockMvc.perform(delete("/customers")
                        .param("email", "eduardo@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuário removido com sucesso"));
    }

    private Usuario criarUsuario(String nome, String email, String documento) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setDocumento(documento);
        return usuario;
    }
}
