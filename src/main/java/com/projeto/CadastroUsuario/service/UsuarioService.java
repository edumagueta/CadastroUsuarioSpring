package com.projeto.CadastroUsuario.service;

import com.projeto.CadastroUsuario.dto.UsuarioRequest;
import com.projeto.CadastroUsuario.model.Usuario;
import com.projeto.CadastroUsuario.repository.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario salvarUsuario(UsuarioRequest request) {

        if (request.dataNascimento() == null || request.dataNascimento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Informe uma data de nascimento válida.");
        }
        if (request.email() == null || request.email().isBlank()) {
            throw new IllegalArgumentException("Informe o e-mail.");
        }
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new EmailDuplicadoException();
        }

        Usuario usuario = new Usuario();

        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setDataNascimento(request.dataNascimento());
        usuario.setPublicacaoPendente(true);

        try {
            return usuarioRepository.saveAndFlush(usuario);
        } catch (DataIntegrityViolationException erro) {
            if (usuarioRepository.existsByEmail(request.email())) {
                throw new EmailDuplicadoException();
            }
            throw erro;
        }
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }

    public List<Usuario> buscarUsuariosPorNome(String nome) {
        return usuarioRepository.findByNomeIgnoreCase(nome);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }

    public List<Usuario> buscarPorDocumento(String documento) {
        return usuarioRepository.findByDocumento(documento);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public boolean deletarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(usuario -> {
                    usuarioRepository.delete(usuario);
                    return true;
                })
                .orElse(false);
    }
}
