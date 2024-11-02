package com.projeto.CadastroUsuario.repository;

import com.projeto.CadastroUsuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNome(String nome);
    void deleteByNome(String nome);
}