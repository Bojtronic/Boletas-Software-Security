package com.funcional.proyecto.controllers;

import com.funcional.proyecto.databases.Usuarios;
import com.funcional.proyecto.models.IUsuario;
import com.funcional.proyecto.models.RolesEnum;
import com.funcional.proyecto.models.Usuario;
import org.springframework.ui.Model;

public abstract  class SeguridadControllers {
    protected final Usuarios Usuarios;
    private Usuario userTemp;
    public SeguridadControllers(IUsuario usuarioRepository) {
        Usuarios = new Usuarios(usuarioRepository);
    }

    protected void checkCookie(String cookie) throws Exception {
        if (cookie.isEmpty())
            throw new Exception("La sesión ya expiró, por favor volver a iniciar sesión");

        int cv = Integer.parseInt(cookie);
        Usuario user = Usuarios.getUsuarioById(cv);
        if (user == null)
            throw new Exception("Problemas de autenticación de usuario, volver a inicar sesión");
        
        userTemp = user;
    }

    protected boolean failCookie(String cookie) {
        if (cookie.isEmpty())
            return true;

        int cv = Integer.parseInt(cookie);
        Usuario user = Usuarios.getUsuarioById(cv);
        if (user == null)
            return true;
        
        userTemp = user;
        return false;
    }

    protected void getHeader(Model model) {
        if (userTemp != null) {
            if (userTemp.getRole() == RolesEnum.Técnico)
                model.addAttribute("headerRole", "header-tecnico");
            if (userTemp.getRole() == RolesEnum.Empleado)
                model.addAttribute("headerRole", "header-empleado");
        }
    }
}
