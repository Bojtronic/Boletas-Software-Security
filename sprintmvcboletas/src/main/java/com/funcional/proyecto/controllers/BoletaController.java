package com.funcional.proyecto.controllers;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funcional.proyecto.databases.Boletas;
import com.funcional.proyecto.databases.Clientes;
import com.funcional.proyecto.databases.Servicios;
import com.funcional.proyecto.databases.Productos;
import com.funcional.proyecto.models.Boleta;
import com.funcional.proyecto.models.BoletaFiltro;
import com.funcional.proyecto.models.BoletaRequest;
import com.funcional.proyecto.models.IBoleta;
import com.funcional.proyecto.models.IBoletaServicio;
import com.funcional.proyecto.models.ICliente;
import com.funcional.proyecto.models.IProducto;
import com.funcional.proyecto.models.IServicio;
import com.funcional.proyecto.models.IUsuario;
import com.funcional.proyecto.models.ResponseAPI;
import com.funcional.proyecto.models.RolesEnum;
import com.google.gson.Gson;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BoletaController extends SeguridadControllers {

    private final Boletas Boletas;
    private final Clientes Clientes;
    private final Servicios Servicios;
    private final Productos Productos;

    public BoletaController(IBoleta respository, IBoletaServicio BServicioRepository, IUsuario usuarioRepository,
            ICliente clienteRepository, IServicio servicioRepository, IProducto productosRespository) {
        super(usuarioRepository);
        this.Boletas = new Boletas(respository, BServicioRepository);
        this.Clientes = new Clientes(clienteRepository);
        this.Servicios = new Servicios(servicioRepository);
        this.Productos = new Productos(productosRespository);
    }

    // Paginas
    // -------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "boletas/paging", method = RequestMethod.GET)
    public String List(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "tecnico", required = false) String tecnico,
            @RequestParam(value = "cliente", required = false) String cliente,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            @RequestParam(value = "productos", required = false) String productos,
            @RequestParam(value = "finial", required = false) String finicial,
            @RequestParam(value = "ffinal", required = false) String ffinal,
            @CookieValue(value = "_id", defaultValue = "") String cookieValue,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        int _id = id.isEmpty() ? 0 : Integer.parseInt(id);

        LocalDateTime FechaInicio = finicial.isEmpty() ? null : LocalDateTime.parse(finicial);
        LocalDateTime FechaFinal = finicial.isEmpty() ? null : LocalDateTime.parse(ffinal);

        model.addAttribute("boletasList",
                Boletas.List(_id, tecnico, cliente, descripcion, productos, FechaInicio, FechaFinal));
        model.addAttribute("filter",
                new BoletaFiltro(_id, tecnico, cliente, descripcion, productos, FechaInicio, FechaFinal));
        model.addAttribute("messageError", "");

        return "boleta/list";
    }

    @RequestMapping(value = "boleta/detalle/{id}", method = RequestMethod.GET)
    public String Detail(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
        Boleta data = Boletas.get(_id);

        if (data == null) {
            model.addAttribute("boletasList", Boletas.List(_id, "", "", "", "", null, null));
            model.addAttribute("filter", new BoletaFiltro(_id, "", "", "", "", null, null));
            model.addAttribute("messageError", "Registro de boleta no encontrado");
            return "boleta/list";
        }

        model.addAttribute("boleta", data);

        return "boleta/detail";
    }

    @GetMapping("boleta/nuevo")
    public String Nuevo(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        model.addAttribute("clientList", Clientes.List("", "", "", "", "", ""));

        model.addAttribute("tecnicosList",
                Usuarios.List(0, "", "", "", "", "", "").stream().filter(u -> u.getRole() == RolesEnum.Técnico)
                        .toList());
    
        model.addAttribute("productos", Productos.getAll());
        model.addAttribute("servicios", Servicios.getAll());

        return "boleta/create";
    }

    @RequestMapping(value = "boleta/update/{id}", method = RequestMethod.GET)
    public String Update(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        

        int _id = id.isEmpty() ? 0 : Integer.parseInt(id);

        Boleta data = Boletas.get(_id);

        if (data == null) {
            model.addAttribute("boletasList", Boletas.List(_id, "", "", "", "", null, null));
            model.addAttribute("filter", new BoletaFiltro(_id, "", "", "", "", null, null));
            model.addAttribute("messageError", "Registro de boleta no encontrado");
            return "boleta/list";
        }

        model.addAttribute("clientList", Clientes.List("", "", "", "", "", ""));
        model.addAttribute("tecnicosList",
                Usuarios.List(0, "", "", "", "", "", "").stream().filter(u -> u.getRole() == RolesEnum.Técnico)
                        .toList());
        model.addAttribute("boleta", data);
        model.addAttribute("productos", Productos.getAll());
        model.addAttribute("servicios", Servicios.getAll());

        return "boleta/update";
    }
    // -------------------------------------------------------------------------------------------------------------

    // Metodos API
    // -------------------------------------------------------------------------------------------------------------
    @PostMapping("api/boleta/nuevo")
    @ResponseBody
    public ResponseAPI newBoleta(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @RequestBody String entity) {
        try {

            checkCookie(cookieValue);

            Gson gson = new Gson();
            // System.out.println("json: -> " + entity);
            BoletaRequest request = gson.fromJson(entity, BoletaRequest.class);
            Boletas.Nuevo(request);
            return new ResponseAPI("Creado con éxito");
        } catch (Exception e) {
            return new ResponseAPI().AsError(e.getMessage());
        }
    }

    @PutMapping("api/boleta/{id}")
    @ResponseBody
    public ResponseAPI updateBoleta(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id,
            @RequestBody String entity) {

        try {

            checkCookie(cookieValue);

            Gson gson = new Gson();
            //System.out.println("json: -> " + entity);
            BoletaRequest request = gson.fromJson(entity, BoletaRequest.class);
            int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
            Boletas.Update(_id, request);
            return new ResponseAPI("Actualización de boleta con éxito");
        } catch (Exception e) {
            return new ResponseAPI().AsError(e.getMessage());
        }
    }

    @DeleteMapping("api/boleta/delete/{id}")
    @ResponseBody
    public ResponseAPI deleteBoleta(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id) {

        try {
            checkCookie(cookieValue);

            int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
            Boletas.Delete(_id);
            return new ResponseAPI("Registro borrado con éxito");
        } catch (Exception e) {
            return new ResponseAPI().AsError(e.getMessage());
        }
    }

    @GetMapping("api/boleta/servicios")
    @ResponseBody
    public ResponseAPI getServicios(@CookieValue(value = "_id", defaultValue = "") String cookieValue) {
        try {
            checkCookie(cookieValue);

            return new ResponseAPI(this.Servicios.getAll());
        } catch (Exception e) {
            return new ResponseAPI().AsError(e.getMessage());
        }
    }

    @GetMapping("api/boleta/productos")
    @ResponseBody
    public ResponseAPI getProductos(@CookieValue(value = "_id", defaultValue = "") String cookieValue) {
        try {
            checkCookie(cookieValue);

            return new ResponseAPI(this.Productos.getAll());
        } catch (Exception e) {
            return new ResponseAPI().AsError(e.getMessage());
        }
    }

    // -------------------------------------------------------------------------------------------------------------
}
