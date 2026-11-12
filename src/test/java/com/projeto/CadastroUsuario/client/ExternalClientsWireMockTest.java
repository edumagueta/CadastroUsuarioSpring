package com.projeto.CadastroUsuario.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.projeto.CadastroUsuario.client.credit.CreditAnalysisClient;
import com.projeto.CadastroUsuario.client.federal.FederalManagerClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ExternalClientsWireMockTest {
    private WireMockServer wireMock;

    @BeforeEach
    void iniciarWireMock() {
        wireMock = new WireMockServer(0);
        wireMock.start();
        configureFor("localhost", wireMock.port());
    }

    @AfterEach
    void encerrarWireMock() {
        wireMock.stop();
    }

    @Test
    void consultaFederalManagerUsandoRespostaMockada() {
        wireMock.stubFor(get(urlEqualTo("/federal-manager/Eduardo"))
                .willReturn(okJson("{\"numero_cpf\":\"123.456.789-00\",\"situacao_cadastral\":\"REGULAR\"}")));

        var resposta = new FederalManagerClient(wireMock.baseUrl()).consultar("Eduardo");

        assertEquals("123.456.789-00", resposta.numero_cpf());
        assertEquals("REGULAR", resposta.situacao_cadastral());
    }

    @Test
    void consultaCreditAnalysisUsandoRespostaMockada() {
        wireMock.stubFor(get(urlPathEqualTo("/credit-analysis/person"))
                .withQueryParam("document", equalTo("123.456.789-00"))
                .willReturn(okJson("{\"document\":\"123.456.789-00\",\"creditScore\":812,\"rating\":\"A\"}")));

        var resposta = new CreditAnalysisClient(wireMock.baseUrl())
                .consultar("123.456.789-00");

        assertEquals(812, resposta.creditScore());
        assertEquals("A", resposta.rating());
    }
}
