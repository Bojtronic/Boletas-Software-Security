package com.funcional.proyecto.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funcional.proyecto.models.IUsuario;
import com.funcional.proyecto.models.ResetPasswordRequest;
import com.funcional.proyecto.models.ResponseAPI;
import com.funcional.proyecto.models.RolesEnum;
import com.funcional.proyecto.models.Usuario;
import com.funcional.proyecto.models.UsuarioRequest;
import com.google.gson.Gson;

@Controller
public class UsuarioController extends SeguridadControllers {

    public UsuarioController(IUsuario repository) {
        super(repository);
    }

    // Paginado
    // ------------------------------------------------------------------------------------------
    @RequestMapping(value = "usuarios/paging", method = RequestMethod.GET)
    public String List(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "apellidos", required = false) String apellidos,
            @RequestParam(value = "cedula", required = false) String cedula,
            @RequestParam(value = "correo", required = false) String correo,
            @RequestParam(value = "user", required = false) String username,
            @RequestParam(value = "role", required = false) String role,
            @CookieValue(value = "_id", defaultValue = "") String cookieValue,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        model.addAttribute("userList", Usuarios.List(0, nombre, apellidos, cedula, correo, username, role));
        model.addAttribute("filter", new Usuario(nombre, apellidos, cedula, correo, username, role));
        model.addAttribute("roles", Stream.of(RolesEnum.values()).map(RolesEnum::name).collect(Collectors.toList()));
        model.addAttribute("messageError", "");

        return "usuario/list";
    }

    @RequestMapping(value = "usuario/detalle/{id}", method = RequestMethod.GET)
    public String Detail(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
        Usuario data = Usuarios.getUsuarioById(_id);
        if (data == null) {
            model.addAttribute("userList", Usuarios.List(0, "", "", "", "", "", ""));
            model.addAttribute("roles", Arrays.asList(RolesEnum.values()));
            model.addAttribute("messageError", "Registro de usuario no encontrado");
            return "usuario/list";
        }

        model.addAttribute("usuario", data);

        return "usuario/detail";
    }

    @RequestMapping(value = "usuario/nuevo", method = RequestMethod.GET)
    public String create(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        List<RolesEnum> roles = Arrays.asList(RolesEnum.values());
        model.addAttribute("roles", roles.stream().filter(r -> r != RolesEnum.None).toList());

        return "usuario/create";
    }

    @RequestMapping(value = "usuario/update/{id}", method = RequestMethod.GET)
    public String Update(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
        Usuario data = Usuarios.getUsuarioById(_id);
        model.addAttribute("roles", Stream.of(RolesEnum.values()).map(RolesEnum::name).collect(Collectors.toList()));
        if (data == null) {
            model.addAttribute("userList", Usuarios.List(0, "", "", "", "", "", ""));
            model.addAttribute("messageError", "Registro de usuario no encontrado");
            return "usuario/list";
        }

        model.addAttribute("usuario", data);
        return "usuario/update";
    }

    // ------------------------------------------------------------------------------------------

    // APIS
    // ------------------------------------------------------------------------------------------
    @PostMapping("api/usuario/nuevo")
    @ResponseBody
    public ResponseAPI Nuevo(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @RequestBody String entity) {
        try {

            checkCookie(cookieValue);

            Gson gson = new Gson();
            System.out.println("json: -> " + entity);
            UsuarioRequest request = gson.fromJson(entity, UsuarioRequest.class);
            if (Usuarios.usuarioNoAvailable(request.getUser()))
                return new ResponseAPI().AsError("El usuario suministrado, no se encuentra disponible");

            Usuarios.Nuevo(request);
            return new ResponseAPI("Usuario creado con éxito!!");
        } catch (Exception e) {
            return new ResponseAPI().AsError(e.getMessage());
        }
    }

    @PutMapping("api/usuario/{id}")
    @ResponseBody
    public ResponseAPI UpdateUsuario(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id,
            @RequestBody String entity) {
        try {
            checkCookie(cookieValue);

            Gson gson = new Gson();
            System.out.println("json: -> " + entity);
            UsuarioRequest request = gson.fromJson(entity, UsuarioRequest.class);
            int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
            Usuarios.Update(_id, request);
            return new ResponseAPI("Usuario actualizado con éxito");
        } catch (Exception e) {
            return new ResponseAPI().AsError(e.getMessage());
        }
    }

    @PutMapping("api/usuario/resetPassword/{id}")
    @ResponseBody
    public ResponseAPI putMethodName(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id,
            @RequestBody String entity) {
        try{

            checkCookie(cookieValue);

            Gson gson = new Gson();
            System.out.println("json: -> " + entity);
            ResetPasswordRequest request = gson.fromJson(entity, ResetPasswordRequest.class);

            int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
            if(!Usuarios.resetPassword(_id, request.getPassword())){
                return new ResponseAPI().AsError("Error al intentar reinciar la contraseña");
            }

            return new ResponseAPI(request.getPassword());

        } catch(Exception e){
            return new ResponseAPI().AsError(e.getMessage());
        }     
        
    }

    @DeleteMapping("api/usuario/{id}")
    @ResponseBody
    public ResponseAPI DeleteUsusario(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id) {
        try {

            checkCookie(cookieValue);

            int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
            Usuarios.Delete(_id);
            return new ResponseAPI("Usuario eliminado con éxito");
        } catch (Exception e) {
            return new ResponseAPI().AsError(e.getMessage());
        }
    }
    // ------------------------------------------------------------------------------------------
}
