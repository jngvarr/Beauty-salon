package ru.jngvarr.storagemanagement.controllers;

import dao.entities.Consumable;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.jngvarr.storagemanagement.service.StorageService;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/storage")
@CrossOrigin(origins = "http://localhost:4200")
public class StorageController {
    private final StorageService storageService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<Consumable> getConsumables() {
        log.debug("get");
        List<Consumable> cons = storageService.getConsumables();
        return cons;
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public Consumable getConsumable(@PathVariable Long id) {
        return storageService.getConsumable(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/byTitle/{title}")
    public List<Consumable> getConsumable(@PathVariable String title) {
        log.debug("title {}", title);
        return storageService.getConsumableByTitle(title);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public Consumable add(@RequestBody Consumable consumable) {
        log.debug("consumable = {}", consumable.getUnit());
        log.debug("consumable name = {}", consumable.getUnit().getName());
        return storageService.add(consumable);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public Consumable update(@RequestBody Consumable newData, @PathVariable Long id) {
        return storageService.update(newData, id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        storageService.delete(id);
    }
}
