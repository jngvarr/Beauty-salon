package ru.jngvarr.clientmanagement.controllers;

import dao.entities.people.Client;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.jngvarr.clientmanagement.services.ClientService;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<Client> showAll(@RequestHeader Map<String, String> headers) {
        headers.forEach((key, value) -> {
            System.out.printf("Header '%s' = %s%n", key, value);
        });
        return clientService.getClients();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/clear")
    public void clearAllData() {
        clientService.clearAllData();
    }

    @GetMapping("/{id}")
    public Client getClient(@PathVariable Long id) {
        return clientService.getClient(id);
    }

    @GetMapping("/by-contact/{phoneNumber}")
    public List<Client> getClientByPhone(@PathVariable String phoneNumber) {
        log.debug("number={}", phoneNumber);

        return clientService.getClientByContact(phoneNumber);
    }

    @GetMapping("/by-name")
    public List<Client> getClientsByName(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "lastName", required = false) String lastName) {
        log.debug("name={}, lastname={}", name, lastName);
        return (name.isEmpty() || lastName.isEmpty()) ?
                (name.isEmpty() ? clientService.getClientByLastName(lastName) : clientService.getClientByName(name)) :
                clientService.getClientByFullName(name, lastName);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public Client createClient(@Valid @RequestBody Client newClient) {
        log.debug("create-client {}", newClient);
        return clientService.addClient(newClient);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public Client updateClient(@RequestBody Client newClient, @PathVariable Long id) {
        return clientService.update(newClient, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public void deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
    }
}
