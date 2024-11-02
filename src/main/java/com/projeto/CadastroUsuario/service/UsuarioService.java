package com.projeto.CadastroUsuario.service;

import com.projeto.CadastroUsuario.model.Usuario;
import com.projeto.CadastroUsuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario salvarUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarUsuarioPorNome(String nome){
        return usuarioRepository.findByNome(nome);
    }

    public List<Usuario> listarUsuarios(){
        return usuarioRepository.findAll();
    }

    public boolean deletarUsuarioPorNome(String nome){
        Optional<Usuario> usuario = usuarioRepository.findByNome(nome);
        if (usuario.isPresent()){
            usuarioRepository.delete(usuario.get());
            return true;
        } else {
            return false;
        }
    }
}