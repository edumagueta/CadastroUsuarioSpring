package com.projeto.CadastroUsuario.service;

public class EmailDuplicadoException extends IllegalArgumentException {
    public EmailDuplicadoException() {
        super("Já existe um usuário cadastrado com este e-mail.");
    }
}
