package com.funcional.proyecto.models;


public class ResponseAPI {
    private Object data;
    private Cookie cookie;
    private String errorMessage;
    private Boolean succees;

    public ResponseAPI(){
        
    }

    public ResponseAPI(Object _data){
        this.data = _data;
        this.succees = true;
        this.errorMessage = "";
    }

    public ResponseAPI(Object _data, Cookie _cookie){
        this.data = _data;
        this.succees = true;
        this.errorMessage = "";
        this.cookie = _cookie;
    }

    public ResponseAPI AsError(String error){
        this.data = null;
        this.succees = false;
        this.errorMessage = error;
        return this;
    }

    public Object getData(){
        return data;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    public Boolean isSuccess(){
        return succees;
    }

    public void setCookie(Cookie _cookie){
        this.cookie = _cookie;
    }

    public Cookie getCookie(){
        return this.cookie;
    }
    
}
