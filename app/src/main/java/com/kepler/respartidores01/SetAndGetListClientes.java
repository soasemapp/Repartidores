package com.kepler.respartidores01;

public class SetAndGetListClientes {

    String Clave;
    String Nombre;

    public SetAndGetListClientes(String clave, String nombre) {
        Clave = clave;
        Nombre = nombre;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
