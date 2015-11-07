package com.example.diego.proyecto;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Diego on 01/11/2015.
 */
public class Anuncio {

    int id;
    String titulo;
    Date fecha;
    String descripcion;
    String[] numeros;

    public int getId() {return id;}
    public String getTitulo(){return titulo;}
    public Date getFecha(){return fecha;}
    public String getDescripcionn(){return descripcion;}
    public String[] getNumeros(){return numeros;}

    public Anuncio(int id, String titulo, Date fecha, String descripcion, String[] numeros){
        this.id = id;
        this.titulo = titulo;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.numeros = numeros;
    }


}
