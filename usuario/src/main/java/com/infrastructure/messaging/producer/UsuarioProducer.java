package com.infrastructure.messaging.producer;

import com.infrastructure.messaging.dto.UsuarioMensagem;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UsuarioProducer {
    private final RabbitTemplate rabbitTemplate;
    private final String responseQueue;

    @Autowired
    public UsuarioProducer(RabbitTemplate rabbitTemplate,
                                    @Value("${rabbitmq.queue.usuario.response}") String responseQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.responseQueue = responseQueue;
    }

    public void enviarResposta(UsuarioMensagem message) {
        rabbitTemplate.convertAndSend(responseQueue, message);
    }
}
