package com.funcional.proyecto.models;

public class RendimientoTecnicosView implements IExportModel{
    private Usuario tecnico;
    private int cantidadServicios;
    private long duracionServicios;
    private double promedioDuracionServicios;

    public Usuario getTecnico() {
        return tecnico;
    }
    public void setTecnico(Usuario tecnico) {
        this.tecnico = tecnico;
    }
    public int getCantidadServicios() {
        return cantidadServicios;
    }
    public void setCantidadServicios(int cantidadServicios) {
        this.cantidadServicios = cantidadServicios;
    }
    public long getDuracionServicios() {
        return duracionServicios;
    }
    public void setDuracionServicios(long duracionServicios) {
        this.duracionServicios = duracionServicios;
    }
    public double getPromedioDuracionServicios() {
        return promedioDuracionServicios;
    }
    public void setPromedioDuracionServicios(double promedioDuracionServicios) {
        this.promedioDuracionServicios = promedioDuracionServicios;
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
                value = String.format("%1$-16s", "N Servicios");
                break;
            case 2:
                value = String.format("%1$-20s", "Duración Servicios");
                break;
            case 3:
                value = String.format("%1$-20s", "Promedio Duración Servicios");
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
                value = String.format("%1$-16s", String.valueOf(cantidadServicios));
                break;
            case 2:
                value = String.format("%1$-20s", String.valueOf(duracionServicios));
                break;
            case 3:
                value = String.format("%1$-20s", String.valueOf(promedioDuracionServicios));
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
