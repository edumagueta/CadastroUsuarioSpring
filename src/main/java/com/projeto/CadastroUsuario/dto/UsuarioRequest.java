package com.projeto.CadastroUsuario.dto;

import java.time.LocalDate;

public record UsuarioRequest(
        String nome,
        String email,
        LocalDate dataNascimento
) {
}