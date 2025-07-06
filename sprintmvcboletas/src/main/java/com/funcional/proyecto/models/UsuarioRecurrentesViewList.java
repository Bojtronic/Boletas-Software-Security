package com.funcional.proyecto.models;

public class UsuarioRecurrentesViewList implements IExportModel {
    private String nombreCliente;
    private int cantidadServicios;
    private String servicioComun;     
    
    public String getNombreCliente() {
        return nombreCliente;
    }
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
    public int getCantidadServicios() {
        return cantidadServicios;
    }
    public void setCantidadServicios(int cantidadServicios) {
        this.cantidadServicios = cantidadServicios;
    }
    public String getServicioComun() {
        return servicioComun;
    }
    public void setServicioComun(String servicioComun) {
        this.servicioComun = servicioComun;
    }

    @Override
    public int countHeader() {
        return 3;
    }
    @Override
    public String getHeader(int index) {
        String value = "";
        switch (index) {
            case 0:
                value = String.format("%1$-25s", "Cliente");
                break;
            case 1:
                value = String.format("%1$-18s", "N de Servicios");
                break;
            case 2:
                value = String.format("%1$-30s", "Servicio Común");
                break;
            default:
                break;
        }
        return value;
    }
    @Override
    public boolean isValueArray(int index) {
        return false;
    }
    @Override
    public String getValue(int index) {
        String value = "";
        switch (index) {
            case 0:
                value = String.format("%1$-25s", nombreCliente);
                break;
            case 1:
                value = String.format("%1$-18s", String.valueOf(cantidadServicios));
                break;
            case 2:
                value = String.format("%1$-30s", servicioComun);
                break;
            default:
                break;
        }
        return value;
    }
    @Override
    public int countValueArray() {
        return 0;
    }
    @Override
    public String getSubValue(int index) {
        return "";
    }    
}
