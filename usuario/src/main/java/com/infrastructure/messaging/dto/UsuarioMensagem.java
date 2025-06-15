package com.infrastructure.messaging.dto;


public class UsuarioMensagem {
    private Long usuarioId;
    private boolean valido;

    @SuppressWarnings("unused")
    public UsuarioMensagem() {
    }

    public UsuarioMensagem(Long usuarioId, boolean valido) {
        this.usuarioId = usuarioId;
        this.valido = valido;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    @SuppressWarnings("unused")
    public boolean isValido() {
        return valido;
    }
}