package com.funcional.proyecto.models;

public class BoletaRequest {
    private String idTecnico;
    private String idCliente;
    private ServicioRequest servicio;
    private String firma;

    public String getIdTecnico() {
        return idTecnico;
    }
    public void setIdTecnico(String idTecnico) {
        this.idTecnico = idTecnico;
    }
    public String getIdCliente() {
        return idCliente;
    }
    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }
    public ServicioRequest getServicio() {
        return servicio;
    }
    public void setServicio(ServicioRequest servicio) {
        this.servicio = servicio;
    }
    public String getFirma() {
        return firma;
    }
    public void setFirma(String firma) {
        this.firma = firma;
    }
    
}
