package com.funcional.proyecto.models;

public class Cookie {
    private String name;
    private Object value;
    private int days;

    public Cookie(){

    }

    public Cookie(String name, Object value){
        this.name = name;
        this.value = value;
    }

    public Cookie(String name, Object value, int days){
        this.name = name;
        this.value = value;
        this.days = days;
    }

    public void setName(String _name){
        this.name = _name;
    }

    public void setValue(String _value){
        this.value = _value;
    }

    public void setDays(int _days){
        this.days = _days;
    }

    public String getName(){
        return this.name;
    }

    public Object getValue(){
        return this.value;
    }

    public int getDays(){
        return this.days;
    }
}
