package com.funcional.proyecto.databases;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import com.funcional.proyecto.models.IServicio;
import com.funcional.proyecto.models.Servicio;
import com.funcional.proyecto.models.ServicioItemRequest;

public class Servicios extends BaseModel {
    private IServicio repository;

    public Servicios(IServicio servicioRepository){
        this.repository = servicioRepository;
    }

    public List<Servicio> getAll(){
        Iterable<Servicio> source = repository.findAll();
        List<Servicio> lista = new ArrayList<Servicio>();
        source.forEach(lista::add);
        return lista.stream().filter(s -> s.isActive()).toList();        
    }

    public List<Servicio> list(String descripcion){
        Iterable<Servicio> source = repository.findAll();
        List<Servicio> lista = new ArrayList<Servicio>();
        source.forEach(lista::add);
        var filter = lista.stream().filter(s -> s.isActive() && Compare(descripcion, s.getDescripcion())).toList();  
        return filter.stream().sorted((s1, s2)-> Integer.compare(s1.getId(),s2.getId())).toList();
    }

    public Servicio get(int id){
        Optional<Servicio> entity = repository.findById(id);
        if(entity.isPresent()){
            return entity.get();
        }
        return null;
    }

    public void nuevo(ServicioItemRequest request){
        Servicio entity = new Servicio();
        entity.setDescripcion(request.getDescripcion());
        repository.save(entity);
    }

    public void update(int id, ServicioItemRequest request){
        Optional<Servicio> entity = repository.findById(id);
        if(entity.isPresent()){
            entity.get().setDescripcion(request.getDescripcion());
            repository.save(entity.get());
        }
    }

    public void delete(int id){
        Optional<Servicio> entity = repository.findById(id);
        if(entity.isPresent()){
            entity.get().setActive(false);
            repository.save(entity.get());
        }
    }
}
