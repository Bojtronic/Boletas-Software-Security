package com.funcional.proyecto.models;

public class TiempoRespuestaView implements IExportModel {
    private Usuario tecnico;
    private long promedioRespuesta;
    private int cantidadServicios;
    private String estandar;

    public Usuario getTecnico() {
        return tecnico;
    }
    public void setTecnico(Usuario tecnico) {
        this.tecnico = tecnico;
    }
    public long getPromedioRespuesta() {
        return promedioRespuesta;
    }
    public void setPromedioRespuesta(long promedioRespuesta) {
        this.promedioRespuesta = promedioRespuesta;
    }
    public String getEstandar() {
        return estandar;
    }
    public void setEstandar(String estandar) {
        this.estandar = estandar;
    }
    public int getCantidadServicios() {
        return cantidadServicios;
    }
    public void setCantidadServicios(int cantidadServicios) {
        this.cantidadServicios = cantidadServicios;
    }

    @Override
    public int countHeader() {
        return 4;
    }
    @Override
    public String getHeader(int index) {
        String value = "";
        switch (index) {
            case 0:
                value = String.format("%1$-25s", "Técnico");
                break;
            case 1:
                value = String.format("%1$-22s", "Promedio Respuesta");
                break;
            case 2:
                value = String.format("%1$-22s", "Cantidad Servicios");
                break;
             case 3:
                value = String.format("%1$-10s", "Estandar");
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
                value = String.format("%1$-25s", tecnico.getNombre() + " " + tecnico.getApellidos());
                break;
            case 1:
                value = String.format("%1$-22s", promedioRespuesta);
                break;
            case 2:
                value = String.format("%1$-22s", cantidadServicios);
                break;
             case 3:
                value = String.format("%1$-10s", estandar);
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
