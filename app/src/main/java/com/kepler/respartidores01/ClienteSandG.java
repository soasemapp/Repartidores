package com.kepler.respartidores01;

public class ClienteSandG {
    String Clave;
    String Nombre;
    String Direccion;

    public ClienteSandG(String clave, String nombre, String direccion) {
        Clave = clave;
        Nombre = nombre;
        Direccion = direccion;
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

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }
}

