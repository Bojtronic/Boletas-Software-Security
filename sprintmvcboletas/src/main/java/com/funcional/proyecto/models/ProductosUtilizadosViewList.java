package com.funcional.proyecto.models;

import java.util.List;

public class ProductosUtilizadosViewList {
    private List<ProductosUtilizadosView> productos;
    private double costoTotal;

    public List<ProductosUtilizadosView> getProductos() {
        return productos;
    }
    public void setProductos(List<ProductosUtilizadosView> productos) {
        this.productos = productos;
    }
    public double getCostoTotal() {
        return costoTotal;
    }
    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }    
}
