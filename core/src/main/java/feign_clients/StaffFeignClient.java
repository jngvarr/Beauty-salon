package feign_clients;

import dao.entities.people.Employee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "staff", configuration = JwtFeignConfig.class)
public interface StaffFeignClient {

    @GetMapping("/api/staff")
    List<Employee> getEmployees();

    @GetMapping("/api/staff/{id}")
    Employee getEmployee(@PathVariable Long id);

    @GetMapping("/api/staff/by-phone/{phoneNumber}")
    Employee getEmployeeByPhone(@PathVariable String phoneNumber);

    @RequestMapping(value = "/api/staff/create", method = RequestMethod.POST)
    Employee addEmployee(@RequestBody Employee employeeToAdd);

    @RequestMapping(value = "/api/staff/update/{id}", method = RequestMethod.PUT)
    Employee update(@RequestBody Employee newData, @PathVariable Long id);

    @RequestMapping(value = "/api/staff/delete/{id}", method = RequestMethod.DELETE)
    void deleteEmployee(@PathVariable Long id);

}
