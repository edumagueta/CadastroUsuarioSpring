package com.projeto.CadastroUsuario.dto;

import com.projeto.CadastroUsuario.model.Usuario;

import java.time.LocalDate;
import java.time.Period;

public class UsuarioDTO {
    private String nome;
    private String email;
    private LocalDate dataNascimento;

    public UsuarioDTO() {
    }

    public UsuarioDTO(String nome, String email, LocalDate dataNascimento) {
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Usuario toEntity() {
        Usuario usuario = new Usuario();
        usuario.setNome(this.nome);
        usuario.setEmail(this.email);
        usuario.setDataNascimento(this.dataNascimento);
        return usuario;
    }

    public static UsuarioDTO fromEntity(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setDataNascimento(usuario.getDataNascimento());
        return dto;
    }
}


