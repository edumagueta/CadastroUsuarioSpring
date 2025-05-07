package com.projeto.CadastroUsuario.service;

import com.projeto.CadastroUsuario.model.Usuario;
import com.projeto.CadastroUsuario.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    UsuarioService usuarioService;

    @Mock
    UsuarioRepository usuarioRepository;

    @Test
    void testBuscarUsuarioPorNome(){
        Mockito.when(usuarioRepository.findByNome("Eduardo"))
                .thenReturn(Optional.of(new Usuario()));

        Optional<Usuario> usuario = usuarioService.buscarUsuarioPorNome("Eduardo");
        assertTrue(usuario.isPresent(), "O usuário deveria estar presente, mas não está.");
    }

    @Test
    void testDeletarUsuarioPorNome_Sucess(){
        Usuario usuario = new Usuario();
        Mockito.when(usuarioRepository.findByNome(any()))
                        .thenReturn(Optional.of(usuario));
        Mockito.doNothing().when(usuarioRepository).delete(usuario);

        boolean resultado = usuarioService.deletarUsuarioPorNome("Eduardo");

        assertTrue(resultado);
        Mockito.verify(usuarioRepository).delete(usuario);
    }

    @Test
    void testDeletarUsuarioPorNome_Fail(){
        Mockito.when(usuarioRepository.findByNome("Desconhecido")).thenReturn(Optional.empty());
        boolean resultado = usuarioService.deletarUsuarioPorNome("Desconhecido");
        Assertions.assertFalse(resultado);
    }

    @Test
    void testSalvarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("Eduardo");
        Mockito.when(usuarioRepository.save(any())).thenReturn(usuario);

        Usuario salvo = usuarioService.salvarUsuario(usuario);
        assertEquals("Eduardo", salvo.getNome());
    }

    @Test
    void testListarUsuarios() {
        List<Usuario> usuarios = List.of(new Usuario(), new Usuario());
        Mockito.when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.listarUsuarios();
        assertEquals(2, resultado.size());
    }
}