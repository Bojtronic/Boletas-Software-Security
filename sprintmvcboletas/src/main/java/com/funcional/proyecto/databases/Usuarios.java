package com.funcional.proyecto.databases;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.funcional.proyecto.models.IUsuario;
import com.funcional.proyecto.models.Usuario;
import com.funcional.proyecto.models.UsuarioRequest;

public class Usuarios extends BaseModel {

    private final IUsuario repository;

    public Usuarios(IUsuario usuarioRepositorio) {
        this.repository = usuarioRepositorio;
    }

    // AUTH
    // --------------------------------------------------------------------------------------------------
    public Usuario getUsuarioByUser(String user) {
        Iterable<Usuario> source = repository.findAll();
        List<Usuario> lista = new ArrayList<Usuario>();
        source.forEach(lista::add);

        if (lista.stream().anyMatch(u -> u.getUser().equals(user))) {
            return lista.stream().filter(u -> u.getUser().equals(user)).findFirst().get();
        }
        return null;
    }

    public Usuario getUsuarioById(int id) {
        Optional<Usuario> usuario = repository.findById(id);
        if (usuario.isPresent())
            return usuario.get();
        return null;
    }

    public boolean usuarioNoAvailable(String user) {
        Iterable<Usuario> source = repository.findAll();
        List<Usuario> lista = new ArrayList<Usuario>();
        source.forEach(lista::add);

        if (lista.stream().anyMatch(u -> u.getUser().equals(user))) {
            return true;
        }
        return false;
    }

    public boolean checkLogin(String user, String password) {

        Iterable<Usuario> source = repository.findAll();
        List<Usuario> lista = new ArrayList<Usuario>();
        source.forEach(lista::add);

        if (lista.stream().anyMatch(u -> u.getUser().equals(user))) {
            if (lista.stream().anyMatch(u -> u.getPassword().equals(password))) {
                Usuario usuario = lista.stream().filter(u -> u.getUser().equals(user)).findFirst().get();
                if (usuario.getFailLogins() > 3)
                    return false;

                return true;
            }
            // fallo de contraseña
            Usuario usuario = lista.stream().filter(u -> u.getUser().equals(user)).findFirst().get();            
            usuario.setFailLogins(usuario.getFailLogins() + 1);
            repository.save(usuario);
        }

        return false;
    }

    public boolean isBlocked(String user) {
        Iterable<Usuario> source = repository.findAll();
        List<Usuario> lista = new ArrayList<Usuario>();
        source.forEach(lista::add);

        if (lista.stream().anyMatch(u -> u.getUser().equals(user))) {
            Usuario usuario = lista.stream().filter(u -> u.getUser().equals(user)).findFirst().get();
            if (usuario.getFailLogins() >= 3)
                return true;
        }
        return false;
    }

    public boolean resetPassword(int id, String newPassword){
        Optional<Usuario> usuario = repository.findById(id);
        if(usuario.isPresent()){
            Usuario entity = usuario.get();
            entity.setFailLogins(0);
            entity.setPassword(newPassword);            
            repository.save(entity);
            return true;
        }

        return false;
    }
    // --------------------------------------------------------------------------------------------------

    public List<Usuario> List(int id, String nombre, String apellidos, String cedula, String correo, String user,
            String role) {
        Iterable<Usuario> source = repository.findAll();
        List<Usuario> lista = new ArrayList<Usuario>();
        source.forEach(lista::add);

        List<Usuario> data = lista.stream().filter(c ->  c.isActive() &&
                (Compare(nombre, c.getNombre()))
                && (Compare(apellidos, c.getApellidos()))
                && (Compare(cedula, c.getCedula()))
                && (Compare(correo, c.getCorreo()))
                && (Compare(user, c.getUser()))
                && (CompareEquals(role, c.getRoleString()))
                )
                .toList();
        return data;
    }

    public void Delete(int id) {
        Optional<Usuario> usuario = repository.findById(id);
        if(usuario.isPresent()){
            Usuario entity = usuario.get();
            entity.setActive(false);
            repository.save(entity);
        }
    }

    public void Nuevo(UsuarioRequest request) {
        Usuario entity = new Usuario();
        entity.setNombre(request.getNombre());
        entity.setApellidos(request.getApellidos());
        entity.setCedula(request.getCedula());
        entity.setCorreo(request.getCorreo());
        entity.setRole(request.getRole());
        entity.setPassword(request.getPassword());
        entity.setUser(request.getUser());
        repository.save(entity);
    }

    public void Update(int id, UsuarioRequest request) {
        Optional<Usuario> usuario = repository.findById(id);
        if (usuario.isPresent()) {
            Usuario entity = usuario.get();
            entity.setNombre(request.getNombre());
            entity.setApellidos(request.getApellidos());
            entity.setCedula(request.getCedula());
            entity.setCorreo(request.getCorreo());
            entity.setRole(request.getRole());
            if (request.getPassword() != null)
                entity.setPassword(request.getPassword());
            entity.setUser(request.getUser());
            repository.save(entity);
        }

    }
}
