package com.projeto.CadastroUsuario.service;

import com.projeto.CadastroUsuario.dto.UsuarioRequest;
import com.projeto.CadastroUsuario.model.Usuario;
import com.projeto.CadastroUsuario.repository.UsuarioRepository;
import com.projeto.CadastroUsuario.messaging.UsuarioProducer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioProducer usuarioProducer;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioProducer usuarioProducer) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioProducer = usuarioProducer;
    }

    public Usuario salvarUsuario(UsuarioRequest request) {

        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
        }

        Usuario usuario = new Usuario();

        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setDataNascimento(request.dataNascimento());

        Usuario salvo = usuarioRepository.save(usuario);
        usuarioProducer.solicitarEnriquecimento(salvo.getId());
        return salvo;
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }

    public List<Usuario> buscarUsuariosPorNome(String nome) {
        return usuarioRepository.findByNome(nome);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
    }

    public Usuario buscarPorDocumento(String documento) {
        return usuarioRepository.findByDocumento(documento)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));
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
