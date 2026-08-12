package com.kepler.respartidores01.SetAndGet;

public class SucursalSANDG {
   String clave;
   String nombre;

    public SucursalSANDG(String clave, String nombre) {
        this.clave = clave;
        this.nombre = nombre;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
