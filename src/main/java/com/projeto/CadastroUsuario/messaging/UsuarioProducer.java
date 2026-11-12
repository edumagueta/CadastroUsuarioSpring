package com.projeto.CadastroUsuario.messaging;

import com.projeto.CadastroUsuario.model.Usuario;
import com.projeto.CadastroUsuario.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class UsuarioProducer {
    private static final Logger log = LoggerFactory.getLogger(UsuarioProducer.class);
    private final KafkaTemplate<String, Long> kafkaTemplate;
    private final UsuarioRepository usuarioRepository;
    private final String topic;

    public UsuarioProducer(KafkaTemplate<String, Long> kafkaTemplate,
                           UsuarioRepository usuarioRepository,
                           @Value("${app.kafka.topic.usuario-enriquecimento}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.usuarioRepository = usuarioRepository;
        this.topic = topic;
    }

    @Scheduled(fixedDelayString = "${app.kafka.publicacao.intervalo-ms:5000}")
    public void publicarPendentes() {
        for (Usuario usuario : usuarioRepository.findTop20ByPublicacaoPendenteTrueOrderByIdAsc()) {
            Long id = usuario.getId();
            try {
                kafkaTemplate.send(topic, id.toString(), id).get(5, TimeUnit.SECONDS);
                usuarioRepository.confirmarPublicacao(id);
            } catch (InterruptedException erro) {
                Thread.currentThread().interrupt();
                return;
            } catch (Exception erro) {
                log.warn("Publicação do enriquecimento do usuário {} será tentada novamente.", id, erro);
                return;
            }
        }
    }
}
