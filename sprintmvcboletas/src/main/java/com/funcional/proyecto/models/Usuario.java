package com.funcional.proyecto.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Usuarios")
public class Usuario extends Persona {

    @Column(name = "userName")
    private String user;
    @Column(name = "userPassword")
    private String password;
    private RolesEnum role = RolesEnum.None;
    private int failLogins = 0;

    public Usuario() {

    }

    public Usuario(String nombre){
        this.nombre = nombre;
    }

    public Usuario(String nombre, String apellidos, String cedula, String correo, String user, String role){
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.correo = correo;
        this.user = user;
        this.role = role.isEmpty() ? RolesEnum.None : RolesEnum.valueOf(role);
    }

    public Usuario(int id, String nombre, String apellidos, String cedula, String correo, String user,
            String password, RolesEnum role) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.correo = correo;
        this.user = user;
        this.password = password;
        this.role = role;
        this.failLogins = 0;
    }

   

    public void setUser(String _user) {
        this.user = _user;
    }

    public void setPassword(String _password) {
        this.password = _password;
    }

    public void setRole(RolesEnum _role) {
        this.role = _role;
    }

    public void setFailLogins(int _failLogins) {
        this.failLogins = _failLogins;
    }

    public String getUser() {
        return this.user;
    }

    public String getPassword() {
        return this.password;
    }

    public RolesEnum getRole() {
        return this.role;
    }

    public String getRoleString(){
        return this.role == RolesEnum.None ? "Rol..." : this.role.toString();
    }

    public int getFailLogins() {
        return this.failLogins;
    }
}
