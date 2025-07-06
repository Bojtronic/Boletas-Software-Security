package com.funcional.proyecto.models;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class Persona {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected int id;

    protected String nombre;
    protected String apellidos;
    protected String cedula;
    protected String correo;

    @Column(nullable = false, columnDefinition="smallint")
    protected int active = 1;


    public void setId(int _id){
        this.id = _id;
    }
    
    public void setNombre(String _nombre){
        this.nombre = _nombre;
    }

    public void setApellidos(String _apellidos){
        this.apellidos = _apellidos;
    }

    public void setCedula(String _cedula){
        this.cedula = _cedula;
    }

    public void setCorreo(String _correo){
        this.correo = _correo;
    }
    
    public int getId(){
        return this.id;
    }

    public String getNombre(){
        return this.nombre;
    }  

    public String getApellidos(){
        return this.apellidos;
    }

    public String getCedula(){
        return this.cedula;
    }

    public String getCorreo(){
        return this.correo;
    }

    public boolean isActive() {
        return active == 1;
    }

    public void setActive(boolean active) {
        this.active = active ? 1 : 0;
    }
}
