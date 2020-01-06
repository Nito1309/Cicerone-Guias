package com.nitoelchidoceti.ciceroneguias.POJOS;

import java.io.Serializable;

public class PojoComentario implements Serializable {

    private String comentario, userName, fecha, FK_lugar;

    public PojoComentario() {
        comentario="";
        userName="";
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFK_lugar() {
        return FK_lugar;
    }

    public void setFK_lugar(String FK_lugar) {
        this.FK_lugar = FK_lugar;
    }
}
