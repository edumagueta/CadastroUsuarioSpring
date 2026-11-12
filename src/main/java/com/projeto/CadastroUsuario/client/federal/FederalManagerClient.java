package com.projeto.CadastroUsuario.client.federal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class FederalManagerClient {
    private final RestClient restClient;

    public FederalManagerClient(@Value("${integrations.federal-manager.base-url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public FederalManagerResponse consultar(String nome) {
        return restClient.get().uri("/federal-manager/{name}", nome)
                .retrieve().body(FederalManagerResponse.class);
    }
}
