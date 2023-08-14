package com.kepler.respartidores01;

public class Mdestallefac {
    String producto;
    String descripcion;
    String cantidad;
    String Preciou;
    String PrecioTotal;

    public Mdestallefac(String producto, String descripcion, String cantidad, String preciou, String precioTotal) {
        this.producto = producto;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.Preciou = preciou;
        this.PrecioTotal = precioTotal;
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

    public String getPreciou() {
        return Preciou;
    }

    public void setPreciou(String preciou) {
        Preciou = preciou;
    }

    public String getPrecioTotal() {
        return PrecioTotal;
    }

    public void setPrecioTotal(String precioTotal) {
        PrecioTotal = precioTotal;
    }
}
