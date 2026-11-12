package com.projeto.CadastroUsuario.client.credit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CreditAnalysisClient {
    private final RestClient restClient;

    public CreditAnalysisClient(@Value("${integrations.credit-analysis.base-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public CreditAnalysisResponse consultar(String documento) {
        return restClient.get().uri(uriBuilder -> uriBuilder.path("/credit-analysis/person")
                        .queryParam("document", documento).build())
                .retrieve().body(CreditAnalysisResponse.class);
    }
}
