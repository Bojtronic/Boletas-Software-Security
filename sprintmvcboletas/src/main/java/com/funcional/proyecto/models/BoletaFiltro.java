package com.funcional.proyecto.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BoletaFiltro {
    private int id;
    private String cliente;
    private String tecnico;
    private String servicio;
    private String producto;
    private LocalDateTime fInicio;
    private LocalDateTime fFinal;

    

    public BoletaFiltro(int id, String cliente, String tecnico, String servicio, String producto, LocalDateTime fInicio,
            LocalDateTime fFinal) {
        this.id = id;
        this.cliente = cliente;
        this.tecnico = tecnico;
        this.servicio = servicio;
        this.producto = producto;
        this.fInicio = fInicio;
        this.fFinal = fFinal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public LocalDateTime getfInicio() {
        return fInicio;
    }

    public void setfInicio(LocalDateTime fInicio) {
        this.fInicio = fInicio;
    }

    public LocalDateTime getfFinal() {
        return fFinal;
    }

    public void setfFinal(LocalDateTime fFinal) {
        this.fFinal = fFinal;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public Boolean getFInicioHasValue(){
        return this.fInicio != null;
    }

    public String getFInicioToStringFormat() {
        if(!getFInicioHasValue())
            return "0000-00-00 00:00:00";

        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        String formattedDate = this.fInicio.format(myFormatObj);
        return formattedDate;
    } 

    public Boolean getFFinalHasValue(){
        return this.fFinal != null;
    }

    public String getFFinalToStringFormat() {
        if(!getFFinalHasValue())
            return "0000-00-00 00:00:00";
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        String formattedDate = this.fFinal.format(myFormatObj);
        return formattedDate;
    }
}
