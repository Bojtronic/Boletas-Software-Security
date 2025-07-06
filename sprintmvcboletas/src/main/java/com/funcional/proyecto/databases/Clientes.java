package com.funcional.proyecto.databases;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import com.funcional.proyecto.models.Cliente;
import com.funcional.proyecto.models.ClienteRequest;
import com.funcional.proyecto.models.ICliente;

public class Clientes extends BaseModel {
    private final ICliente repository;

    public Clientes(ICliente clienteRepository) {
        this.repository = clienteRepository;
    }

    public List<Cliente> List(String nombre, String apellidos, String cedula, String correo, String direccion,
            String contacto) {
        Iterable<Cliente> source = repository.findAll();
        List<Cliente> lista = new ArrayList<Cliente>();
        source.forEach(lista::add);

        List<Cliente> data = lista.stream().filter(c -> (Compare(nombre, c.getNombre()))
                && (Compare(apellidos, c.getApellidos()))
                        && (Compare(cedula.trim(), c.getCedula()))
                        && (Compare(correo, c.getCorreo()))
                        && (Compare(direccion, c.getDireccion()))
                        && (Compare(contacto.trim(), c.getNumContacto()))
                        && c.isActive())
                .toList();
        return data;
    }

    public void Delete(int id) {
        Optional<Cliente> cliente = repository.findById(id);
        if(cliente.isPresent()){
            Cliente entity = cliente.get();
            entity.setActive(false);
            repository.save(entity);
        }
    }

    public void Nuevo(ClienteRequest request) {
        Cliente entity = new Cliente();
        entity.setNombre(request.getNombre());
        entity.setApellidos(request.getApellidos());
        entity.setCedula(request.getCedula());
        entity.setCorreo(request.getCorreo());
        entity.setDireccion(request.getDireccion());
        entity.setNumContacto(request.getNumContacto());
        repository.save(entity);
    }

    public void Update(int id, ClienteRequest request) {
        Optional<Cliente> cliente = repository.findById(id);
        if(cliente.isPresent()){
            Cliente entity = cliente.get();
            entity.setNombre(request.getNombre());
            entity.setApellidos(request.getApellidos());
            entity.setCedula(request.getCedula());
            entity.setCorreo(request.getCorreo());
            entity.setDireccion(request.getDireccion());
            entity.setNumContacto(request.getNumContacto());
            repository.save(entity);
        }
    }

    public Cliente get(int id) {
        Optional<Cliente> cliente = repository.findById(id);
        if(cliente.isPresent()){
            return cliente.get();
        }
        return null;
    }
}
