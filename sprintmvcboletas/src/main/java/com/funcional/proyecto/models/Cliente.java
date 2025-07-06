package com.funcional.proyecto.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Clientes")
public class Cliente extends Persona {
    private String direccion;
    private String numContacto;

    public Cliente(){

    }

    public Cliente(String nombre){
        this.nombre = nombre;
    }

    public Cliente(int id, String nombre, String apellidos, String cedula, String correo, String direccion, String numContacto){
        this.id = id;
        this.nombre = nombre;
        this.apellidos =  apellidos;
        this.cedula = cedula;
        this.correo = correo;
        this.direccion = direccion;
        this.numContacto = numContacto;
    }

    public Cliente(String nombre, String apellidos, String cedula, String correo, String direccion, String numContacto){
        this.nombre = nombre;
        this.apellidos =  apellidos;
        this.cedula = cedula;
        this.correo = correo;
        this.direccion = direccion;
        this.numContacto = numContacto;
    }

    public void setDireccion(String _direccion){
        this.direccion = _direccion;
    }

    public void setNumContacto(String _numContacto){
        this.numContacto = _numContacto;
    }

    public String getDireccion(){
        return this.direccion;
    }

    public String getNumContacto(){
        return this.numContacto;
    }
}
