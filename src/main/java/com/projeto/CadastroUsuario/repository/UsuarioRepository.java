package com.projeto.CadastroUsuario.repository;

import com.projeto.CadastroUsuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByNomeIgnoreCase(String nome);
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findByDocumento(String documento);
    List<Usuario> findTop20ByPublicacaoPendenteTrueOrderByIdAsc();

    @Transactional
    @Modifying
    @Query("update Usuario u set u.publicacaoPendente = false where u.id = :id")
    void confirmarPublicacao(Long id);

    @Transactional
    @Modifying
    @Query("""
            update Usuario u set u.documento = :documento, u.indicadorRegularidade = :regular,
                u.scoreCredito = :score where u.id = :id
            """)
    void atualizarEnriquecimento(Long id, String documento, boolean regular, Integer score);
}
