package com.application.service;

import com.infrastructure.messaging.dto.UsuarioMensagem;
import com.infrastructure.messaging.producer.UsuarioProducer;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UsuarioService {

    private final UsuarioProducer usuarioProducer;
    private final ConcurrentHashMap<Long, CompletableFuture<Boolean>> validacoesPendentes = new ConcurrentHashMap<>();

    public UsuarioService(UsuarioProducer uP) {
        this.usuarioProducer = uP;
    }

    public CompletableFuture<Boolean> validarUsuario(Long usuarioId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        validacoesPendentes.put(usuarioId, future);

        usuarioProducer.enviarRequisicao(new UsuarioMensagem(usuarioId, false));
        return future;
    }

    public void receberResultadoMensagem(UsuarioMensagem message) {
        CompletableFuture<Boolean> future = validacoesPendentes.remove(message.getUsuarioId());
        if (future != null) {
            future.complete(message.isValido());
        }
    }
}