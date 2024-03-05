package ru.jngvarr.clientmanagment.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.jngvarr.clientmanagment.services.ClientService;
import ru.jngvarr.clientmanagment.model.Client;


@Controller
@RequiredArgsConstructor
public class ClientsController {
    private final ClientService clientService;

    @GetMapping("/clients")
    public String showAll(Model model) {
        model.addAttribute("clients", clientService.showAll());
        return "clients";
    }

    public String getClient(Model model, @RequestBody Client client){

    }
}
