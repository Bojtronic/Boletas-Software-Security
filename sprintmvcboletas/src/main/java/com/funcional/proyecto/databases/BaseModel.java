package com.funcional.proyecto.databases;

import java.util.List;

import com.funcional.proyecto.models.Producto;

public class BaseModel {

    enum FindFecha{
        before,
        after
    }

    enum Findfield{
        descripcion,
        productos
    }

    protected boolean Compare(String value1, String value2) {
        return value1.isEmpty() || value2.toLowerCase().contains(value1.trim().toLowerCase());
    }

    protected boolean Compare(double value1, double value2){
        return value1 == -1 || Double.toString(value1).contains(Double.toString(value2));
    }

    protected boolean CompareEquals(String value1, String value2) {
        return value1.isEmpty() || value2.equalsIgnoreCase(value1);
    }

    

    protected boolean Compare(String value1, List<Producto> productos) {
        return value1.isEmpty()
                || productos.stream().anyMatch(p -> p.getNombre().toLowerCase().contains(value1.toLowerCase()));
    }
}
