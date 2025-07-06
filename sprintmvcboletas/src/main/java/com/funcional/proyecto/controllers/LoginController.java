package com.funcional.proyecto.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.funcional.proyecto.models.Cookie;
import com.funcional.proyecto.models.IUsuario;
import com.funcional.proyecto.models.ResponseAPI;
import com.funcional.proyecto.models.Usuario;
import com.funcional.proyecto.models.UsuarioLoginRequest;
import com.google.gson.Gson;


@Controller
public class LoginController extends SeguridadControllers {
    
    public LoginController(IUsuario repository) {
        super(repository);
    }

    @GetMapping("login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("logout")
    public String logout(){
        return "auth/logout";
    }

    @PostMapping("api/login")
    @ResponseBody()
    public ResponseAPI login(@RequestBody String entity) {
        Gson gson = new Gson();
        

        UsuarioLoginRequest request = gson.fromJson(entity, UsuarioLoginRequest.class);
        //usuarios existe, y la contraseña esta correcta
        if (Usuarios.checkLogin(request.getUser(), request.getPassword())) {
            Usuario user = Usuarios.getUsuarioByUser(request.getUser());
            //verificamos una vez el usuario
            if(user != null){
                //se retorna bien y se guarda en una cookie la data                
                return new ResponseAPI("home", new Cookie("_id", user.getId()));
            }                    
        } 

         //verificamos si el usuario ya esta bloqueado
         if (Usuarios.isBlocked(request.getUser())) {
            return new ResponseAPI().AsError("Usuario se encontra bloqueado, demasiados intentos de conexión");
        }
        return new ResponseAPI().AsError("Usuario o contraseña incorrecta");
    }

    @GetMapping("api/usuario/{id}")
    @ResponseBody
    public ResponseAPI getUsuario(@CookieValue(value = "_id", defaultValue = "") String cookieValue,
            @PathVariable(value = "id") String id) {
        
                try {
                    
                    checkCookie(cookieValue);

                    int _id = id.isEmpty() ? 0 : Integer.parseInt(id);
                    Usuario tec = Usuarios.getUsuarioById(_id);
                    if(tec != null){
                        return new ResponseAPI(tec);
                    }

                    return new ResponseAPI().AsError("Usuario no encontrado");
                    
                } catch (Exception e) {
                    return new ResponseAPI().AsError("Error de consulta de usuario");
                }        
    }
    

}
