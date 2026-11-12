package com.projeto.CadastroUsuario.dto;

import com.projeto.CadastroUsuario.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        LocalDate dataNascimento
) {

    public static UsuarioResponse fromEntity(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getDataNascimento()
        );
    }
}
