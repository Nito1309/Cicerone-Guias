package com.nitoelchidoceti.ciceroneguias.POJOS;

public class PojoMensaje {
    private String mensaje,Nombre,type_mensaje,urlFoto, idUsuario,nombreDestinatario, fotoPerfilDestinatario;;

    public PojoMensaje() {
    }

    public PojoMensaje(String mensaje, String nombre, String type_mensaje, String urlFoto,
                       String idUsuario, String nombreDestinatario, String fotoPerfilDestinatario) {
        this.mensaje = mensaje;
        Nombre = nombre;
        this.type_mensaje = type_mensaje;
        this.urlFoto = urlFoto;
        this.idUsuario = idUsuario;
        this.nombreDestinatario = nombreDestinatario;
        this.fotoPerfilDestinatario = fotoPerfilDestinatario;
    }

    public PojoMensaje(String mensaje, String nombre, String type_mensaje, String idUsuario,
                       String nombreDestinatario, String fotoPerfilDestinatario) {
        this.mensaje = mensaje;
        Nombre = nombre;
        this.type_mensaje = type_mensaje;
        this.idUsuario = idUsuario;
        this.nombreDestinatario = nombreDestinatario;
        this.fotoPerfilDestinatario = fotoPerfilDestinatario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getType_mensaje() {
        return type_mensaje;
    }

    public void setType_mensaje(String type_mensaje) {
        this.type_mensaje = type_mensaje;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario = nombreDestinatario;
    }

    public String getFotoPerfilDestinatario() {
        return fotoPerfilDestinatario;
    }

    public void setFotoPerfilDestinatario(String fotoPerfilDestinatario) {
        this.fotoPerfilDestinatario = fotoPerfilDestinatario;
    }
}
