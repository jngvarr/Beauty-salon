package ru.jngvarr.staffmanagement.controllers;

import dao.entities.people.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.jngvarr.staffmanagement.services.StaffService;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/staff")
public class StaffController {
    private final StaffService staffService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public List<Employee> getEmployees() {
//        log.debug("getEmployees {}", staffService.getEmployees());
        return staffService.getEmployees();
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return staffService.getEmployee(id);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/by-phone/{phoneNumber}")
    public Employee getEmployeeByPhone(@PathVariable String phoneNumber) {
        return staffService.getEmployeeByPhone(phoneNumber);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public Employee createEmployee(@RequestBody Employee employee) {
        return staffService.addEmployee(employee);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public Employee updateEmployee(@RequestBody Employee newData, @PathVariable Long id) {
        return staffService.updateEmployee(newData, id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        staffService.delete(id);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/by-name")
    public List<Employee> getEmployeesByName(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "lastName", required = false) String lastName) {
        log.debug("name={}, lastname={}", name, lastName);
        return (name.isEmpty() || lastName.isEmpty()) ?
                (name.isEmpty() ? staffService.getEmployeesByLastName(lastName) : staffService.getEmployeesByName(name)) :
                staffService.getEmployeesByFullName(name, lastName);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/by-contact/{phoneNumber}")
    public List<Employee> getEmployeesByPhone(@PathVariable String phoneNumber) {
        log.debug("number={}", phoneNumber);
        return staffService.getEmployeeByContact(phoneNumber);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/by-function/{function}")
    public List<Employee> getEmployeeByFunction(@PathVariable String function) {
        log.debug("number={}", function);
        return staffService.getEmployeeByFunction(function);
    }
}
