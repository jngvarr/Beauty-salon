package ru.jngvarr.servicemanagement.controllers;

import dao.entities.Servize;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ru.jngvarr.servicemanagement.services.ServiceForServices;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/services")
public class ServiceController {
    private final ServiceForServices service;

    @GetMapping
    public List<Servize> getServices() {
        return service.getServices();
    }

    @GetMapping("/{id}")
    public Servize getService(@PathVariable Long id) {
        return service.getService(id);
    }

    @GetMapping("/duration/{id}")
    public int getServiceDuration(@PathVariable Long id) {
        return service.getServiceDuration(id);
    }

    @PostMapping("/create")
    public Servize addService(@RequestBody Servize newService) {
        return service.addService(newService);
    }

    @PutMapping("/update/{id}")
    public Servize updateService(@RequestBody Servize newData, @PathVariable Long id) {
        return service.update(newData, id);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }


}