package com.projeto.CadastroUsuario.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Setter
@Getter
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    private LocalDate dataNascimento;

    private String documento;

    private Boolean indicadorRegularidade;
    private Integer scoreCredito;
    private boolean publicacaoPendente;

    public Integer getIdade() {
        return calcularIdade(LocalDate.now());
    }

    public Integer calcularIdade(LocalDate dataReferencia) {
        return dataNascimento == null ? null : Period.between(dataNascimento, dataReferencia).getYears();
    }
}
