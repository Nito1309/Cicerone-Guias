package com.nitoelchidoceti.ciceroneguias.POJOS;

import java.io.Serializable;

public class MensajeRecibir extends PojoMensaje implements Serializable {
    private Long hora;

    public MensajeRecibir() {
    }

    public MensajeRecibir(Long hora) {
        this.hora = hora;
    }

    public MensajeRecibir(String mensaje, String nombre, String type_mensaje, String urlFoto, String idUsuario, String nombreDestinatario, Long hora) {
        super(mensaje, nombre, type_mensaje, urlFoto, idUsuario, nombreDestinatario);
        this.hora = hora;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }
}
