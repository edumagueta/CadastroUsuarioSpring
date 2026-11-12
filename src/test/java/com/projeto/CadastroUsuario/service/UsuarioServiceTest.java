package com.projeto.CadastroUsuario.service;

import com.projeto.CadastroUsuario.model.Usuario;
import com.projeto.CadastroUsuario.repository.UsuarioRepository;
import com.projeto.CadastroUsuario.dto.UsuarioRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;
    @Test
    void deveBuscarUsuariosPorNome() {
        Usuario usuario = criarUsuario("Eduardo", "eduardo@gmail.com", "12345678900");
        when(usuarioRepository.findByNomeIgnoreCase("eduardo")).thenReturn(List.of(usuario));

        List<Usuario> resultado = usuarioService.buscarUsuariosPorNome("eduardo");

        assertEquals(1, resultado.size());
        assertEquals("Eduardo", resultado.get(0).getNome());
    }

    @Test
    void deveSalvarUsuario() {
        when(usuarioRepository.saveAndFlush(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario salvo = invocation.getArgument(0);
            salvo.setId(42L);
            return salvo;
        });

        Usuario salvo = usuarioService.salvarUsuario(new UsuarioRequest("Eduardo", "eduardo@gmail.com",
                LocalDate.of(1990, 5, 20)));

        assertNotNull(salvo);
        assertEquals("Eduardo", salvo.getNome());
        assertTrue(salvo.isPublicacaoPendente());
        assertEquals(34, salvo.calcularIdade(LocalDate.of(2025, 5, 19)));
        assertEquals(35, salvo.calcularIdade(LocalDate.of(2025, 5, 20)));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        when(usuarioRepository.existsByEmail("eduardo@gmail.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                EmailDuplicadoException.class,
                () -> usuarioService.salvarUsuario(new UsuarioRequest("Eduardo", "eduardo@gmail.com",
                        LocalDate.of(1990, 5, 20)))
        );

        assertEquals("Já existe um usuário cadastrado com este e-mail.", exception.getMessage());
        verify(usuarioRepository, never()).saveAndFlush(any());
    }

    @Test
    void deveTratarEmailCadastradoEntreConsultaESalvamento() {
        when(usuarioRepository.existsByEmail("eduardo@gmail.com")).thenReturn(false, true);
        when(usuarioRepository.saveAndFlush(any(Usuario.class)))
                .thenThrow(new DataIntegrityViolationException("Email duplicado"));

        assertThrows(EmailDuplicadoException.class,
                () -> usuarioService.salvarUsuario(new UsuarioRequest("Eduardo", "eduardo@gmail.com",
                        LocalDate.of(1990, 5, 20))));
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
