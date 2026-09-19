package com.projeto.CadastroUsuario.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
@AllArgsConstructor
public class UsuarioRequest {

    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private String documento;

    public UsuarioRequest() {
    }

}