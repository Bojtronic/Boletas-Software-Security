package com.funcional.proyecto.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.funcional.proyecto.databases.Productos;
import com.funcional.proyecto.models.IProducto;
import com.funcional.proyecto.models.IUsuario;
import com.funcional.proyecto.models.Producto;
import com.funcional.proyecto.models.ProductoRequest;
import com.funcional.proyecto.models.ResponseAPI;
import com.google.gson.Gson;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@Controller
public class ProductoController extends SeguridadControllers {
    private final Productos Productos;

    public ProductoController(IProducto repository, IUsuario usuarioRepository) {
        super(usuarioRepository);
        Productos = new Productos(repository);
    }

    // Paginas
    // -------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "producto/paging", method = RequestMethod.GET)
    public String List(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "precio", required = false) String precio,
            @CookieValue(value = "_id", defaultValue = "") String cookieValue,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        double _precio = precio.isEmpty() ? -1 : Double.parseDouble(precio);

        model.addAttribute("productosList", Productos.list(nombre, _precio));
        model.addAttribute("filter", new Producto(nombre, _precio));
        model.addAttribute("messageError", "");

        return "producto/list";
    }

    @RequestMapping(value = "producto/detalle/{id}", method = RequestMethod.GET)
    public String Detail(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
        Producto data = Productos.get(_id);

        if (data == null) {
            model.addAttribute("productosList", Productos.list("nombre", -1));
            model.addAttribute("filter", new Producto("nombre", -1));
            model.addAttribute("messageError", "Registro de producto no encontrado");
        }

        model.addAttribute("producto", data);

        return "producto/detail";
    }

    @GetMapping("producto/nuevo")
    public String Nuevo(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);
        return "producto/create";
    }

    @RequestMapping(value = "producto/update/{id}", method = RequestMethod.GET)
    public String Update(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id,
            Model model) {

        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
        Producto data = Productos.get(_id);

        if (data == null) {
            model.addAttribute("productosList", Productos.list("nombre", -1));
            model.addAttribute("filter", new Producto("nombre", -1));
            model.addAttribute("messageError", "Registro de producto no encontrado");
        }

        model.addAttribute("producto", data);

        return "producto/update";
    }

    // -------------------------------------------------------------------------------------------------------------

    // APIs
    // -------------------------------------------------------------------------------------------------------------
    @PostMapping("api/producto/nuevo")
    @ResponseBody
    public ResponseAPI newProducto(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @RequestBody String entity) {        
        try {
            checkCookie(cookieValue);

            Gson gson = new Gson();
            // System.out.println("json: -> " + entity);
            ProductoRequest request = gson.fromJson(entity, ProductoRequest.class);
            Productos.nuevo(request);
            return new ResponseAPI("Creado con éxito");
        } catch (Exception e) {
            return new ResponseAPI().AsError(e.getMessage());
        }  
    }

    @PutMapping("api/producto/{id}")
    @ResponseBody
    public ResponseAPI updateProducto(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id,
            @RequestBody String entity) {
        
        try {
            checkCookie(cookieValue);

            Gson gson = new Gson();
            int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
            ProductoRequest request = gson.fromJson(entity, ProductoRequest.class);
            Productos.update(_id, request);
            return new ResponseAPI("Actualización de producto con éxito");
        } catch (Exception e) {
            return new ResponseAPI().AsError(e.getMessage());
        }
    }

    @DeleteMapping("api/producto/{id}")
    @ResponseBody
    public ResponseAPI deleteProducto(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable("id") String id) {
        try {           
            checkCookie(cookieValue);

            int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
            Productos.delete(_id);
            return new ResponseAPI("Registro borrado con éxito");
        } catch (Exception e) {
            return new ResponseAPI().AsError(e.getMessage());
        }
    }
    
    // -------------------------------------------------------------------------------------------------------------
}
