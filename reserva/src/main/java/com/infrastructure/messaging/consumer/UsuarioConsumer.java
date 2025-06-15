package com.infrastructure.messaging.consumer;

import com.application.service.UsuarioService;
import com.infrastructure.messaging.dto.UsuarioMensagem;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioConsumer {
    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioConsumer(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.usuario.response}")
    public void consumirRespostaRecebida(UsuarioMensagem mensagem) {
        usuarioService.receberResultadoMensagem(mensagem);
    }
}
