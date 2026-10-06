package com.projeto.CadastroUsuario.service;

import com.projeto.CadastroUsuario.model.Usuario;
import com.projeto.CadastroUsuario.repository.UsuarioRepository;
import com.projeto.CadastroUsuario.dto.UsuarioRequest;
import com.projeto.CadastroUsuario.messaging.UsuarioProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private UsuarioProducer usuarioProducer;

    @Test
    void deveBuscarUsuariosPorNome() {
        Usuario usuario = criarUsuario("Eduardo", "eduardo@gmail.com", "12345678900");
        when(usuarioRepository.findByNome("Eduardo")).thenReturn(List.of(usuario));

        List<Usuario> resultado = usuarioService.buscarUsuariosPorNome("Eduardo");

        assertEquals(1, resultado.size());
        assertEquals("Eduardo", resultado.get(0).getNome());
    }

    @Test
    void deveSalvarUsuario() {
        Usuario usuario = criarUsuario("Eduardo", "eduardo@gmail.com", "12345678900");
        when(usuarioRepository.findByEmail("eduardo@gmail.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario salvo = invocation.getArgument(0);
            salvo.setId(42L);
            return salvo;
        });

        Usuario salvo = usuarioService.salvarUsuario(new UsuarioRequest("Eduardo", "eduardo@gmail.com", null));

        assertNotNull(salvo);
        assertEquals("Eduardo", salvo.getNome());
        verify(usuarioRepository).save(any(Usuario.class));
        verify(usuarioProducer).solicitarEnriquecimento(42L);
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        Usuario usuario = criarUsuario("Eduardo", "eduardo@gmail.com", "12345678900");
        when(usuarioRepository.findByEmail("eduardo@gmail.com")).thenReturn(Optional.of(usuario));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.salvarUsuario(new UsuarioRequest("Eduardo", "eduardo@gmail.com", null))
        );

        assertEquals("Já existe um usuário cadastrado com este e-mail.", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveDeletarUsuarioPorEmailComSucesso() {
        Usuario usuario = criarUsuario("Eduardo", "eduardo@gmail.com", "12345678900");
        when(usuarioRepository.findByEmail("eduardo@gmail.com")).thenReturn(Optional.of(usuario));

        boolean resultado = usuarioService.deletarUsuarioPorEmail("eduardo@gmail.com");

        assertTrue(resultado);
        verify(usuarioRepository).delete(usuario);
    }

    @Test
    void deveRetornarFalsoQuandoUsuarioNaoExisteParaDelete() {
        when(usuarioRepository.findByEmail("desconhecido@gmail.com")).thenReturn(Optional.empty());

        boolean resultado = usuarioService.deletarUsuarioPorEmail("desconhecido@gmail.com");

        assertFalse(resultado);
        verify(usuarioRepository, never()).delete(any());
    }

    private Usuario criarUsuario(String nome, String email, String documento) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setDocumento(documento);
        return usuario;
    }
}
