package com.projeto.CadastroUsuario.service;

import com.projeto.CadastroUsuario.model.Usuario;
import com.projeto.CadastroUsuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Usuario salvarUsuario(Usuario usuario){
        if(usuarioRepository.findByEmail(usuario.getEmail()).isPresent()){
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
        }
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarUsuarioPorNome(String nome){
        return usuarioRepository.findByNome(nome);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Usuário não encontrado."
                ));
    }

    public List<Usuario> listarUsuarios(){
        return usuarioRepository.findAll();
    }

    public boolean deletarUsuarioPorNome(String nome){
        Optional<Usuario> usuario = usuarioRepository.findByNome(nome);
        if (usuario.isPresent()){
            usuarioRepository.delete(usuario.get());
            return true;
        }
            return false;
    }
}