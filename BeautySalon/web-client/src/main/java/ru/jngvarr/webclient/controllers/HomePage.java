package ru.jngvarr.webclient.controllers;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomePage {
    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
