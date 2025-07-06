package com.funcional.proyecto.models;


import java.util.List;


public class UsuarioRecurrentesView {
    private List<UsuarioRecurrentesViewList> data;
    private int FrecuenciaVisitas;

    public List<UsuarioRecurrentesViewList> getData() {
        return data;
    }
    public void setData(List<UsuarioRecurrentesViewList> data) {
        this.data = data;
    }
    public int getFrecuenciaVisitas() {
        return FrecuenciaVisitas;
    }
    public void setFrecuenciaVisitas(int frecuenciaVisitas) {
        FrecuenciaVisitas = frecuenciaVisitas;
    }
}
