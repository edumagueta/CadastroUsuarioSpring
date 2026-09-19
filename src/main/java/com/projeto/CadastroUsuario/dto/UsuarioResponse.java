package com.projeto.CadastroUsuario.dto;

import com.projeto.CadastroUsuario.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class UsuarioResponse {

    private Long id;
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private String documento;
    private Boolean indicadorRegularidade;
    private Integer scoreCredito;

    public UsuarioResponse() {
    }

    public static UsuarioResponse fromEntity(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getDataNascimento(),
                usuario.getDocumento(),
                usuario.getIndicadorRegularidade(),
                usuario.getScoreCredito()
        );
    }

}