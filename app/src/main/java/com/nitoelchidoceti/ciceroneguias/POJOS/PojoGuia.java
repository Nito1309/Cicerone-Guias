package com.nitoelchidoceti.ciceroneguias.POJOS;

import java.io.Serializable;
import java.util.ArrayList;

public class PojoGuia implements Serializable {

    private int Id;
    private String nombre,
            descripcion, telefono, correo,horario, duracion,nombreDelSitio,FK_Sitio, fotografia;
    private Double[] costos;
    private ArrayList<String> idiomas;
    private ArrayList<String> titulos;
    private ArrayList<String> imagenes;

    public PojoGuia() {
        Id = 0;
        nombre = "";
        duracion = "";
        nombreDelSitio = "";
        descripcion ="";
        FK_Sitio="";
        horario="";
        telefono="";
        fotografia="null";
        correo="";
        idiomas = new ArrayList<>();
        titulos = new ArrayList<>();
        costos = new Double[3];
        imagenes = new ArrayList<>();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getNombreDelSitio() {
        return nombreDelSitio;
    }

    public void setNombreDelSitio(String nombreDelSitio) {
        this.nombreDelSitio = nombreDelSitio;
    }

    public ArrayList<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(ArrayList<String> idiomas) {
        this.idiomas = idiomas;
    }

    public ArrayList<String> getTitulos() {
        return titulos;
    }

    public void setTitulos(ArrayList<String> titulos) {
        this.titulos = titulos;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public Double[] getCostos() {
        return costos;
    }

    public void setCostos(Double[] costos) {
        this.costos = costos;
    }

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getHorario() {
        return horario;
    }


    public String getFK_Sitio() {
        return FK_Sitio;
    }

    public void setFK_Sitio(String FK_Sitio) {
        this.FK_Sitio = FK_Sitio;
    }

    public ArrayList<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList<String> imagenes) {
        this.imagenes = imagenes;
    }
}
