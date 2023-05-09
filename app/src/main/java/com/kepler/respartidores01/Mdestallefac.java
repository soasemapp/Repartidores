package com.kepler.respartidores01;

public class Mdestallefac {
    String producto;
    String descripcion;
    String cantidad;

    public Mdestallefac(String producto, String descripcion, String cantidad) {
        this.producto = producto;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
