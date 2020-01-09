package com.nitoelchidoceti.ciceroneguias.POJOS;

import java.util.Map;

public class MensajeEnviar extends PojoMensaje{
    private Map hora;

    public MensajeEnviar() {
    }

    public MensajeEnviar(Map hora) {
        this.hora = hora;
    }

    public MensajeEnviar(String mensaje, String nombre, String type_mensaje, String urlFoto,
                         String idUsuario, String nombreDestinatario, String fotoPerfilDestinatario, Map hora) {
        super(mensaje, nombre, type_mensaje, urlFoto, idUsuario, nombreDestinatario, fotoPerfilDestinatario);
        this.hora = hora;
    }

    public MensajeEnviar(String mensaje, String nombre, String type_mensaje, String idUsuario,
                         String nombreDestinatario, String fotoPerfilDestinatario, Map hora) {
        super(mensaje, nombre, type_mensaje, idUsuario, nombreDestinatario, fotoPerfilDestinatario);
        this.hora = hora;
    }

    public Map getHora() {
        return hora;
    }

    public void setHora(Map hora) {
        this.hora = hora;
    }
}
