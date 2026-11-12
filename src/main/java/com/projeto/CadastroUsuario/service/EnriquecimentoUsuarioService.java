package com.projeto.CadastroUsuario.service;

import com.projeto.CadastroUsuario.model.Usuario;
import com.projeto.CadastroUsuario.repository.UsuarioRepository;
import com.projeto.CadastroUsuario.client.federal.FederalManagerClient;
import com.projeto.CadastroUsuario.client.credit.CreditAnalysisClient;
import com.projeto.CadastroUsuario.client.federal.FederalManagerResponse;
import com.projeto.CadastroUsuario.client.credit.CreditAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnriquecimentoUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final FederalManagerClient federalManagerClient;
    private final CreditAnalysisClient creditAnalysisClient;

    public void enriquecerUsuario(Long usuarioId) {
        usuarioRepository.findById(usuarioId)
                .filter(usuario -> usuario.getScoreCredito() == null)
                .ifPresent(this::consultarIntegracoes);
    }

    private void consultarIntegracoes(Usuario usuario) {
        FederalManagerResponse dadosFederais = federalManagerClient.consultar(usuario.getNome());
        String documento = dadosFederais.numero_cpf();
        boolean regular = "REGULAR".equalsIgnoreCase(dadosFederais.situacao_cadastral());
        CreditAnalysisResponse analise = creditAnalysisClient.consultar(documento);
        usuarioRepository.atualizarEnriquecimento(usuario.getId(), documento, regular, analise.creditScore());
    }
}
