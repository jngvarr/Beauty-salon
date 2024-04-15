package ru.jngvarr.staffmanagement.controllers;

import dao.entities.people.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.jngvarr.staffmanagement.services.StaffService;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/staff")
public class StaffController {
    private final StaffService staffService;

    @GetMapping
    public List<Employee> getEmployees() {
//        log.debug("getEmployees {}", staffService.getEmployees());
        return staffService.getEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return staffService.getEmployee(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public Employee createEmployee(@RequestBody Employee employee) {
        return staffService.addEmployee(employee);
    }

    @PutMapping("/update/{id}")
    public Employee updateEmployee(@RequestBody Employee newData, @PathVariable Long id) {
        return staffService.update(newData, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        staffService.delete(id);
    }
}