package com.funcional.proyecto.models;

public class ProductosUtilizadosView implements IExportModel {
    private int idProducto;
    private String producto;
    private int idServicio;
    private String Servicio;
    private int cantidad;
    private double precio;

    public int getIdProducto() {
        return idProducto;
    }
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }
    public String getProducto() {
        return producto;
    }
    public void setProducto(String producto) {
        this.producto = producto;
    }
    public int getIdServicio() {
        return idServicio;
    }
    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }
    public String getServicio() {
        return Servicio;
    }
    public void setServicio(String servicio) {
        Servicio = servicio;
    }
    public int getCantidad() {
        return cantidad;
    }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
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
                value = String.format("%1$-25s", "Producto");
                break;
            case 1:
                value = String.format("%1$-25s", "Servicio");
                break;
            case 2:
                value = String.format("%1$-10s", "Cantidad");
                break;
            case 3:
                value = String.format("%1$-10s", "Precio");        
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
                value = String.format("%1$-25s", producto);
                break;
            case 1:
                value = String.format("%1$-25s", Servicio);
                break;
            case 2:
                value = String.format("%1$-10s", String.valueOf(cantidad));
                break;
            case 3:
                value = String.format("%1$-10s", String.valueOf(precio));        
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
