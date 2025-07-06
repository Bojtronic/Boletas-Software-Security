package com.funcional.proyecto.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funcional.proyecto.databases.Clientes;
import com.funcional.proyecto.models.Cliente;
import com.funcional.proyecto.models.ClienteRequest;
import com.funcional.proyecto.models.ICliente;
import com.funcional.proyecto.models.IUsuario;
import com.funcional.proyecto.models.ResponseAPI;
import com.funcional.proyecto.models.RolesEnum;
import com.funcional.proyecto.models.Usuario;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ClienteController extends SeguridadControllers {

    private final Clientes Clientes;

    public ClienteController(ICliente repository, IUsuario usuarioRepository) {
        super(usuarioRepository);
        Clientes = new Clientes(repository);
    }

    // Paginas
    // -------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "clientes/paging", method = RequestMethod.GET)
    public String List(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "apellidos", required = false) String apellidos,
            @RequestParam(value = "cedula", required = false) String cedula,
            @RequestParam(value = "correo", required = false) String correo,
            @RequestParam(value = "direccion", required = false) String direccion,
            @RequestParam(value = "contacto", required = false) String contacto,
            @CookieValue(value = "_id", defaultValue = "") String cookieValue,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        model.addAttribute("clientList", Clientes.List(nombre, apellidos, cedula, correo, direccion, contacto));
        model.addAttribute("filter", new Cliente(nombre, apellidos, cedula, correo, direccion, contacto));
        model.addAttribute("messageError", "");
        return "cliente/list";
    }

    @RequestMapping(value = "cliente/detalle/{id}", method = RequestMethod.GET)
    public String requestMethodName(
            @CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
        Cliente data = Clientes.get(_id);

        if (data != null)
            model.addAttribute("cliente", data);

        if (data == null) {
            model.addAttribute("clientList", Clientes.List("", "", "", "", "", ""));
            model.addAttribute("filter", new Cliente("", "", "", "", "", ""));
            model.addAttribute("messageError", "Cliente no se encuentra registrado en el sistema");
            return "cliente/list";
        }

        return "cliente/detail";
    }

    @GetMapping("cliente/nuevo")
    public String Create(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        return "cliente/create";
    }

    @RequestMapping(value = "cliente/update/{id}", method = RequestMethod.GET)
    public String update(
            @CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
        Cliente data = Clientes.get(_id);

        if (data != null)
            model.addAttribute("cliente", data);

        if (data == null) {
            model.addAttribute("clientList", Clientes.List("", "", "", "", "", ""));
            model.addAttribute("filter", new Cliente("", "", "", "", "", ""));
            model.addAttribute("messageError", "Cliente no se encuentra registrado en el sistema");
            return "cliente/list";
        }

        return "cliente/update";
    }
    // -------------------------------------------------------------------------------------------------------------

    // API Metodos
    // -------------------------------------------------------------------------------------------------------------
    @GetMapping("api/cliente/{id}")
    @ResponseBody
    public ResponseAPI getCliente(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable(value = "id") String id) {

        try {

            checkCookie(cookieValue);

            int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
            Cliente data = Clientes.get(_id);
            return new ResponseAPI(data);
        } catch (Exception ex) {
            return new ResponseAPI().AsError(ex.getMessage());
        }

    }

    @PostMapping("api/cliente/nuevo")
    @ResponseBody
    public ResponseAPI create(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @RequestBody String entity) {

        try {
            checkCookie(cookieValue);

            Gson gson = new Gson();
            System.out.println("json: -> " + entity);
            ClienteRequest request = gson.fromJson(entity, ClienteRequest.class);
            Clientes.Nuevo(request);
        } catch (Exception e) {
            return new ResponseAPI().AsError(e.getMessage());
        }

        return new ResponseAPI("Nuevo Cliente creado con éxito!");
    }

    @PutMapping("api/cliente/update/{id}")
    @ResponseBody
    public ResponseAPI update(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id,
            @RequestBody String entity) {

        try {
            checkCookie(cookieValue);

            Gson gson = new Gson();
            ClienteRequest request = gson.fromJson(entity, ClienteRequest.class);
            int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
            Clientes.Update(_id, request);
        } catch (Exception e) {
            return new ResponseAPI().AsError(e.getMessage());
        }

        return new ResponseAPI("Cliente actualizado con éxito!");
    }

    @DeleteMapping("api/cliente/delete/{id}")
    @ResponseBody
    public ResponseAPI deleteClient(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable(value = "id") String id) {
        try {

            checkCookie(cookieValue);
            
            int cv = Integer.parseInt(cookieValue);
            Usuario user = Usuarios.getUsuarioById(cv);
            if (user == null)
                return new ResponseAPI().AsError("La sesión ya expiró, por favor volver al login nuevamente");

            Boolean seguir = false;
            if (user != null) {
                if (user.getRole() == RolesEnum.Técnico) {
                    seguir = true;
                }

                if (user.getRole() == RolesEnum.Empleado) {
                    return new ResponseAPI().AsError("No tiene nivel de autorización para realizar dicha acción");
                }
            }
            if (seguir) {
                int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
                Clientes.Delete(_id);
            }

        } catch (Exception ex) {
            return new ResponseAPI().AsError(ex.getMessage());
        }
        return new ResponseAPI("Cliente eliminado");
    }
    // -------------------------------------------------------------------------------------------------------------
}
