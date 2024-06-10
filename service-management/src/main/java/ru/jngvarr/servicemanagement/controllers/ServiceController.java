package ru.jngvarr.servicemanagement.controllers;

import dao.entities.Servize;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.jngvarr.servicemanagement.services.ServiceForServices;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/services")
public class ServiceController {
    private final ServiceForServices service;

    //    @PreAuthorize("isAuthenticated()")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<Servize> getServices() {
        return service.getServices();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public Servize getService(@PathVariable Long id) {
        return service.getService(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/byTitle/{title}")
    public List<Servize> getServicesByTitle(@PathVariable String title) {
        return service.getServicesByTitle(title);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/byDescription/{description}")
    public List<Servize> getServicesByDescription(@PathVariable String description) {
        return service.getServicesByDescription(description);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/byConsumable/{Consumable}")
    public List<Servize> getServicesByConsumable(@PathVariable String consumable) {
        return service.getServicesByConsumable(consumable);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/duration/{duration}")
    public int getServiceDuration(@PathVariable Long duration) {
        return service.getServiceDuration(duration);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public Servize addService(@RequestBody Servize newService) {
        return service.addService(newService);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public Servize updateService(@RequestBody Servize newData, @PathVariable Long id) {
        return service.update(newData, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


}
