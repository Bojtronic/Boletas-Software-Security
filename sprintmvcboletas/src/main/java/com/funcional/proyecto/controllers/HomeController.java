package com.funcional.proyecto.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import com.funcional.proyecto.models.IUsuario;

@Controller
public class HomeController extends SeguridadControllers {

    public HomeController(IUsuario repository) {
        super(repository);
    }

    @GetMapping("")
    public String getDefault(@CookieValue(value = "_id", defaultValue = "") String cookieValue, Model model) {
        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);

        return "home";
    }

    @GetMapping("home")
    public String getHome(@CookieValue(value = "_id", defaultValue = "") String cookieValue, Model model) {
        if (failCookie(cookieValue))
            return "auth/login";

        getHeader(model);
        return "home";
    }

}
