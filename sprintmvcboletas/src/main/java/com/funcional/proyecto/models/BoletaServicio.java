package com.funcional.proyecto.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "BoletaServicio")
public class BoletaServicio {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    //private String descripcion;
    private LocalDateTime  fInicio;
    private LocalDateTime  fFinal;

    @ManyToOne(fetch = FetchType.EAGER)
    private Servicio servicio;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Producto> productos;

    public BoletaServicio() {
    }
    

    public BoletaServicio(LocalDateTime fInicio, LocalDateTime fFinal) {
        this.fInicio = fInicio;
        this.fFinal = fFinal;
    }

    

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }



    public void setFInicio() {
        this.fInicio = LocalDateTime.now();
    }

    public void setFInicio(LocalDateTime _fInicio) {
        this.fInicio = _fInicio;
    }

    public void setFInicio(String _fInicio) {
        if(!_fInicio.isEmpty())
            this.fInicio = LocalDateTime.parse(_fInicio);
    }

    public void setFInicio(int year, int mes, int dia, int hour, int min, int second) {
        this.fInicio = LocalDateTime.of(year, mes, dia, hour, min, second);
    }

    public void setFFinal() {
        this.fFinal = LocalDateTime.now();
    }

    public void setFFinal(LocalDateTime _fFinal) {
        this.fFinal = _fFinal;
    }

    public void setFFinal(String _fFinal) {
        if(!_fFinal.isEmpty())
            this.fFinal = LocalDateTime.parse(_fFinal);
    }

    public void setFFinal(int year, int mes, int dia, int hour, int min, int second) {
        this.fFinal = LocalDateTime.of(year, mes, dia, hour, min, second);
    }

    

    public LocalDateTime getFInicio() {
        return this.fInicio;
    }

    public Boolean getFInicioHasValue(){
        return this.fInicio != null;
    }

    public String getFInicioToString() {
        if(!getFInicioHasValue())
            return "0000-00-00 00:00:00";

        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
        String formattedDate = this.fInicio.format(myFormatObj);
        return formattedDate;
    } 

    public String getFInicioToStringFormat() {
        if(!getFInicioHasValue())
            return "0000-00-00 00:00:00";

        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        String formattedDate = this.fInicio.format(myFormatObj);
        return formattedDate;
    } 

    public LocalDateTime getFFinal() {
        return this.fFinal;
    }

    public Boolean getFFinalHasValue(){
        return this.fFinal != null;
    }

    public String getFFinalToString() {
        if(!getFFinalHasValue())
            return "0000-00-00 00:00:00";
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
        String formattedDate = this.fFinal.format(myFormatObj);
        return formattedDate;
    }

    public String getFFinalToStringFormat() {
        if(!getFFinalHasValue())
            return "0000-00-00 00:00:00";
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        String formattedDate = this.fFinal.format(myFormatObj);
        return formattedDate;
    }


    public List<Producto> getProductos() {
        return productos;
    }


    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public String getProductosString(){
        StringBuilder data = new StringBuilder();
        this.productos.forEach(p -> {            
            data.append((data.isEmpty() ? "" : " / ") + p.getNombre());
        });
        return data.toString();
    }

    public Servicio getServicio() {
        return servicio;
    }


    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    

}
