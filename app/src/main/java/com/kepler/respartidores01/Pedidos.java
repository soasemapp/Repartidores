package com.kepler.respartidores01;

import android.widget.Button;

import java.util.ArrayList;

public class Pedidos {
    String Nombre;
    String Telefonouno;
    String Telefonodos;
    String Folio;
    Button Mapa;

    public Pedidos(String nombre, String telefonouno, String telefonodos, String folio) {
        Nombre = nombre;
        Telefonouno = telefonouno;
        Telefonodos = telefonodos;
        Folio = folio;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getTelefonouno() {
        return Telefonouno;
    }

    public void setTelefonouno(String telefonouno) {
        Telefonouno = telefonouno;
    }

    public String getTelefonodos() {
        return Telefonodos;
    }

    public void setTelefonodos(String telefonodos) {
        Telefonodos = telefonodos;
    }

    public String getFolio() {
        return Folio;
    }

    public void setFolio(String folio) {
        Folio = folio;
    }
}
