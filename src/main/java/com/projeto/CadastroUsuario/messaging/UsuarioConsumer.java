package com.projeto.CadastroUsuario.messaging;

import com.projeto.CadastroUsuario.service.EnriquecimentoUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioConsumer {
    private final EnriquecimentoUsuarioService enriquecimentoUsuarioService;

    @KafkaListener(topics = "${app.kafka.topic.usuario-enriquecimento}")
    public void consumir(Long usuarioId) {
        enriquecimentoUsuarioService.enriquecerUsuario(usuarioId);
    }
}
