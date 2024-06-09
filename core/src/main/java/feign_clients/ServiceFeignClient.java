package feign_clients;

import dao.entities.Servize;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "services", configuration = JwtFeignConfig.class)
public interface ServiceFeignClient {
    @GetMapping("/api/services")
    List<Servize> getServices();
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/api/services/{id}", method = RequestMethod.GET)
    Servize getService(@PathVariable Long id);

    @RequestMapping(value = "/api/services/duration/{id}", method = RequestMethod.GET)
    int getServiceDuration(@PathVariable Long id);

    @PostMapping("/api/services/create")
    void addService(Servize service);

    @RequestMapping(value = "/api/services/update/{id}", method = RequestMethod.PUT)
    Servize updateService(@RequestBody Servize newData, @PathVariable Long id);

    @DeleteMapping("/api/services/delete/{id}")
    void deleteService(@PathVariable Long id);
}
