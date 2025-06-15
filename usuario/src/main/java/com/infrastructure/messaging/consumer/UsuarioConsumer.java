package com.infrastructure.messaging.consumer;

import com.application.service.UsuarioService;
import com.domain.model.Usuario;
import com.infrastructure.messaging.dto.UsuarioMensagem;
import com.infrastructure.messaging.producer.UsuarioProducer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioConsumer {
    private final UsuarioService usuarioService;
    private final UsuarioProducer usuarioValidacaoProducer;

    @Autowired
    public UsuarioConsumer(UsuarioService usuarioService, UsuarioProducer usuarioValidacaoProducer) {
        this.usuarioService = usuarioService;
        this.usuarioValidacaoProducer = usuarioValidacaoProducer;
    }

    @RabbitListener(queues = "${rabbitmq.queue.usuario.request}")
    public void consumer(UsuarioMensagem message) {
        Optional<Usuario> usuarioExiste = usuarioService.findById(message.getUsuarioId());

        boolean usuarioValido = usuarioExiste.isPresent() && usuarioExiste.get().getAtivo();

        UsuarioMensagem response = new UsuarioMensagem(message.getUsuarioId(), usuarioValido);
        usuarioValidacaoProducer.enviarResposta(response);
    }
}
