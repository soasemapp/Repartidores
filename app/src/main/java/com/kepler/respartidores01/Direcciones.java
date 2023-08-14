package com.kepler.respartidores01;

public class Direcciones {
    String Clave;
    String Nombre;
    String Direccion;
    Double Latitud,Longitud;
    String Estatus;
    String Tiempo;
    String Distancia;
    String Distanciatxt;


    public Direcciones(String clave, String nombre, String direccion, Double latitud, Double longitud, String estatus, String tiempo, String distancia, String distanciatxt) {
        Clave = clave;
        Nombre = nombre;
        Direccion = direccion;
        Latitud = latitud;
        Longitud = longitud;
        Estatus = estatus;
        Tiempo = tiempo;
        Distancia = distancia;
        Distanciatxt = distanciatxt;
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

    public Double getLatitud() {
        return Latitud;
    }

    public void setLatitud(Double latitud) {
        Latitud = latitud;
    }

    public Double getLongitud() {
        return Longitud;
    }

    public void setLongitud(Double longitud) {
        Longitud = longitud;
    }

    public String getEstatus() {
        return Estatus;
    }

    public void setEstatus(String estatus) {
        Estatus = estatus;
    }

    public String getTiempo() {
        return Tiempo;
    }

    public void setTiempo(String tiempo) {
        Tiempo = tiempo;
    }

    public String getDistancia() {
        return Distancia;
    }

    public void setDistancia(String distancia) {
        Distancia = distancia;
    }

    public String getDistanciatxt() {
        return Distanciatxt;
    }

    public void setDistanciatxt(String distanciatxt) {
        Distanciatxt = distanciatxt;
    }
}
