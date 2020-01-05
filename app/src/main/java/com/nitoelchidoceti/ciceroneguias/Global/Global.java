package com.nitoelchidoceti.ciceroneguias.Global;


import com.nitoelchidoceti.ciceroneguias.POJOS.PojoGuia;

import java.util.ArrayList;

public final class Global {

    private static Global Turista;
    private String id,nombre ;
    private ArrayList<PojoGuia> guias;
    private Global(){
    }

    public static Global getObject(){
        if (Turista==null){
            Turista=new Global();
        }

        return Turista;
    }

    public String getId() {
        return id;
    }

    public  void setId(String id) {
        this.id = id;
    }

    public ArrayList<PojoGuia> getGuias() {
        return guias;
    }

    public void setGuias(ArrayList<PojoGuia> guias) {
        this.guias = guias;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
