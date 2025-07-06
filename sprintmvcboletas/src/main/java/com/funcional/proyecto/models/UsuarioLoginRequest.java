package com.funcional.proyecto.models;

public class UsuarioLoginRequest {
    private String user;
    private String password;

    public UsuarioLoginRequest(){

    }

    public UsuarioLoginRequest(String _user, String _password){
        this.user = _user;
        this.password = _password;
    }

    public void setUser(String _user){
        this.user = _user;
    }

    public void setPassword(String _password){
        this.password = _password;
    }

    public String getUser(){
        return this.user;
    }

    public String getPassword(){
        return this.password;
    }
}