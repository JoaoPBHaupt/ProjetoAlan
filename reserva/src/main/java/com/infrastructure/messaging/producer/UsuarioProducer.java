package com.infrastructure.messaging.producer;

import com.infrastructure.messaging.dto.UsuarioMensagem;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UsuarioProducer {
    private final RabbitTemplate rabbitTemplate;
    private final String requestQueue;

    public UsuarioProducer(RabbitTemplate rabbitTemplate,
                                    @Value("${rabbitmq.queue.usuario.request}") String requestQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.requestQueue = requestQueue;
    }

    public void enviarRequisicao(UsuarioMensagem message){
        rabbitTemplate.convertAndSend(requestQueue, message);
    }
}
