package com.funcional.proyecto.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Servicios")
public class Servicio {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String descripcion;

    @Column(nullable = false, columnDefinition="smallint")
    protected int active = 1; 

    public Servicio() {
    }    

    public Servicio(int id) {
        this.id = id;
    }  

    public Servicio(String descripcion) {
        this.descripcion = descripcion;
    }    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescripcion(String _descripcion) {
        this.descripcion = _descripcion;
    }
   
    public String getDescripcion() {
        return this.descripcion;
    }    

    public boolean isActive() {
        return active == 1;
    }

    public void setActive(boolean active) {
        this.active = active ? 1 : 0;
    }
}
