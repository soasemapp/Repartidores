package com.kepler.respartidores01;

public class PedidosEntregados {
    String sucursal;
    String cliente;
    String numpaq;
    String Nombre;
    String Telefonouno;
    String Telefonodos;
    String Folio;
    String direccion;
    String recibio;
    String fecha;
    String hora;

    public PedidosEntregados(String sucursal, String cliente, String numpaq, String nombre, String telefonouno, String telefonodos, String folio, String direccion, String recibio, String fecha, String hora) {
        this.sucursal = sucursal;
        this.cliente = cliente;
        this.numpaq = numpaq;
        Nombre = nombre;
        Telefonouno = telefonouno;
        Telefonodos = telefonodos;
        Folio = folio;
        this.direccion = direccion;
        this.recibio = recibio;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getSucursal() {
        return sucursal;
    }

    public void setSucursal(String sucursal) {
        this.sucursal = sucursal;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getNumpaq() {
        return numpaq;
    }

    public void setNumpaq(String numpaq) {
        this.numpaq = numpaq;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRecibio() {
        return recibio;
    }
    public void setRecibio(String recibio) {
        this.recibio = recibio;
    }
    public String getFecha() {
        return fecha;
    }
    public void setHora(String hora) {
        this.hora = hora;
    }
}
