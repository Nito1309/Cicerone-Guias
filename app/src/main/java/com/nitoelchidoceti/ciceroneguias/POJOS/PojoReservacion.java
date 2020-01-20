package com.nitoelchidoceti.ciceroneguias.POJOS;

public class PojoReservacion {
    private String fecha, nombre, registroTurista, telefono;

    public PojoReservacion(String fecha, String nombre, String registroTurista, String telefono) {
        this.fecha = fecha;
        this.nombre = nombre;
        this.registroTurista = registroTurista;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
