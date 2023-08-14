package com.kepler.respartidores01;

import android.widget.Button;

import java.util.ArrayList;

public class Pedidos {
    String sucursal;
    String cliente;
    String numpaq;
    String Nombre;
    String Telefonouno;
    String Telefonodos;
    String Folio;
    String direccion;
    String comentario;
    String status;
    String direccionclave;
    Double latitud;
    Double longitud;
    int Distancia;
    String DistanciaText;
    int Tiempo;
    String TiempoValor;

    public Pedidos(String sucursal, String cliente, String numpaq, String nombre, String telefonouno, String telefonodos, String folio, String direccion, String comentario, String status, String direccionclave, Double latitud, Double longitud, int distancia, String distanciaText, int tiempo, String tiempoValor) {
        this.sucursal = sucursal;
        this.cliente = cliente;
        this.numpaq = numpaq;
        Nombre = nombre;
        Telefonouno = telefonouno;
        Telefonodos = telefonodos;
        Folio = folio;
        this.direccion = direccion;
        this.comentario = comentario;
        this.status = status;
        this.direccionclave = direccionclave;
        this.latitud = latitud;
        this.longitud = longitud;
        Distancia = distancia;
        DistanciaText = distanciaText;
        Tiempo = tiempo;
        TiempoValor = tiempoValor;
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

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDireccionclave() {
        return direccionclave;
    }

    public void setDireccionclave(String direccionclave) {
        this.direccionclave = direccionclave;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public int getDistancia() {
        return Distancia;
    }

    public void setDistancia(int distancia) {
        Distancia = distancia;
    }

    public String getDistanciaText() {
        return DistanciaText;
    }

    public void setDistanciaText(String distanciaText) {
        DistanciaText = distanciaText;
    }

    public int getTiempo() {
        return Tiempo;
    }

    public void setTiempo(int tiempo) {
        Tiempo = tiempo;
    }

    public String getTiempoValor() {
        return TiempoValor;
    }

    public void setTiempoValor(String tiempoValor) {
        TiempoValor = tiempoValor;
    }
}