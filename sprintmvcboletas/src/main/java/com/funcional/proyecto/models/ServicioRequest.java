package com.funcional.proyecto.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ServicioRequest {
    private int id;
    private String fechaInicio;
    private String fechaFinal;
    private List<Producto> productos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }


    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public BoletaServicio getClassTo(){
        BoletaServicio s = new BoletaServicio();
        s.setServicio(new Servicio(this.id));
        s.setProductos(this.productos);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        if(!this.fechaInicio.isEmpty()){
            this.fechaInicio = this.fechaInicio.replace('T', ' ');
            LocalDateTime dateTime = LocalDateTime.parse(this.fechaInicio, formatter);
            s.setFInicio(dateTime);
        }
        
        if(!this.fechaFinal.isEmpty()){
            this.fechaFinal = this.fechaFinal.replace('T', ' ');
            LocalDateTime dateTime = LocalDateTime.parse(this.fechaFinal, formatter);
            s.setFFinal(dateTime);
        }
        
        return s;
    }

    
}
