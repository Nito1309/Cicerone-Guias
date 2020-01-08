package com.nitoelchidoceti.ciceroneguias.POJOS;

public class PojoReservacion {
    private String fecha, titulo, registroTurista, telefono;

    public PojoReservacion(String fecha, String titulo, String registroTurista, String telefono) {
        this.fecha = fecha;
        this.titulo = titulo;
        this.registroTurista = registroTurista;
        this.telefono = telefono;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getRegistroTurista() {
        return registroTurista;
    }

    public void setRegistroTurista(String registroTurista) {
        this.registroTurista = registroTurista;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
