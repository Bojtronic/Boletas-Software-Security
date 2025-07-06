package com.funcional.proyecto.databases;


import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import com.funcional.proyecto.models.IProducto;
import com.funcional.proyecto.models.Producto;
import com.funcional.proyecto.models.ProductoRequest;

public class Productos extends BaseModel {
    private IProducto repository;

    public Productos(IProducto productosRespository){
        this.repository = productosRespository;
    }

    public List<Producto> getAll(){
        Iterable<Producto> source = repository.findAll();
        List<Producto> lista = new ArrayList<Producto>();
        source.forEach(lista::add);
        return lista.stream().filter(p -> p.isActive()).toList();        
    }

    public List<Producto> list(String nombre, double precio){
        Iterable<Producto> source = repository.findAll();
        List<Producto> lista = new ArrayList<Producto>();
        source.forEach(lista::add);
        var filter = lista.stream().filter(p -> p.isActive() && Compare(nombre, p.getNombre()) && Compare(precio, p.getPrecio())).toList(); 
        return filter.stream().sorted((p1, p2) -> Integer.compare(p1.getId(), p2.getId())).toList();       
    }

    public Producto get(int id){
        Optional<Producto> entity = repository.findById(id);
        if(entity.isPresent()){
            return entity.get();
        }
        return null;
    }

    public void nuevo(ProductoRequest request){
        Producto entity = new Producto();
        entity.setNombre(request.getNombre());
        entity.setPrecio(request.getPrecio());        
        repository.save(entity);
    }

    public void update(int id, ProductoRequest request){
        Optional<Producto> entity = repository.findById(id);
        if(entity.isPresent()){
            entity.get().setNombre(request.getNombre());
            entity.get().setPrecio(request.getPrecio());
            repository.save(entity.get());
        }
    }

    public void delete(int id){
        Optional<Producto> entity = repository.findById(id);
        if(entity.isPresent()){
            entity.get().setIsActive(false);
            repository.save(entity.get());
        }
    } 
}
