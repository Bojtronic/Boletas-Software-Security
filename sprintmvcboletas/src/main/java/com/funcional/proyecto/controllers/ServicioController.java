package com.funcional.proyecto.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funcional.proyecto.databases.Servicios;
import com.funcional.proyecto.models.IServicio;
import com.funcional.proyecto.models.IUsuario;
import com.funcional.proyecto.models.ResponseAPI;
import com.funcional.proyecto.models.Servicio;
import com.funcional.proyecto.models.ServicioItemRequest;
import com.google.gson.Gson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class ServicioController extends SeguridadControllers {

    private final Servicios Servicios;

    public ServicioController(IServicio respository, IUsuario usuarioRepository) {
        super(usuarioRepository);
        Servicios = new Servicios(respository);
    }

    // Paginas
    // -------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "servicio/paging", method = RequestMethod.GET)
    public String List(
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @CookieValue(value = "_id", defaultValue = "") String cookieValue,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        model.addAttribute("servicioList", Servicios.list(descripcion));
        model.addAttribute("filter", descripcion);
        model.addAttribute("messageError", "");

        return "servicio/list";
    }

    @RequestMapping(value = "servicio/detalle/{id}", method = RequestMethod.GET)
    public String Detail(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
        Servicio data = Servicios.get(_id);

        if (data == null) {
            model.addAttribute("servicioList", Servicios.list(""));
            model.addAttribute("filter", "");
            model.addAttribute("messageError", "Registro de servicio no encontrado");
            return "servicio/list";
        }

        model.addAttribute("servicio", data);

        return "servicio/detail";
    }

    @GetMapping("servicio/nuevo")
    public String Nuevo(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        return "servicio/create";
    }

    @RequestMapping(value = "servicio/update/{id}", method = RequestMethod.GET)
    public String update(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
        Servicio data = Servicios.get(_id);

        if (data == null) {
            model.addAttribute("servicioList", Servicios.list(""));
            model.addAttribute("filter", "");
            model.addAttribute("messageError", "Registro de boleta no encontrado");
            return "servicio/list";
        }

        model.addAttribute("servicio", data);

        return "servicio/update";
    }

    // -------------------------------------------------------------------------------------------------------------

    // APIs
    // -------------------------------------------------------------------------------------------------------------
    @PostMapping("api/servicio/nuevo")
    @ResponseBody
    public ResponseAPI newServicio(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @RequestBody String entity) {
        try {
            checkCookie(cookieValue);

            Gson gson = new Gson();
            // System.out.println("json: -> " + entity);
            ServicioItemRequest request = gson.fromJson(entity, ServicioItemRequest.class);
            Servicios.nuevo(request);
            return new ResponseAPI("Creado con éxito");
        } catch (Exception e) {
            return new ResponseAPI().AsError(e.getMessage());
        }

    }

    @PutMapping("api/servicio/{id}")
    @ResponseBody
    public ResponseAPI updateServicio(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id,
            @RequestBody String entity) {
        try {
            checkCookie(cookieValue);

            Gson gson = new Gson();
            ServicioItemRequest request = gson.fromJson(entity, ServicioItemRequest.class);
            int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
            Servicios.update(_id, request);
            return new ResponseAPI("Actualización de servicio con éxito");
        } catch (Exception e) {
            return new ResponseAPI().AsError(e.getMessage());
        }
    }

    @DeleteMapping("api/servicio/{id}")
    @ResponseBody
    public ResponseAPI deleteServicio(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id) {
        try {
            checkCookie(cookieValue);

            int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
            Servicios.delete(_id);
            return new ResponseAPI("Registro borrado con éxito");
        } catch (Exception e) {
            return new ResponseAPI().AsError(e.getMessage());
        }
    }

    // -------------------------------------------------------------------------------------------------------------
}
