package feign_clients;

import dao.entities.people.Client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "clients", configuration = JwtFeignConfig.class)
public interface ClientFeignClient {
    @GetMapping("/api/clients")
    List<Client> getClients();

    @GetMapping("/api/clients/{id}")
    Client getClient(@PathVariable Long id);

    @GetMapping("/api/clients/by-contact/{contact}")
    Client getClientByContact(@PathVariable String contact);

    @RequestMapping(value = "/api/clients/create", method = RequestMethod.POST)
    Client addClient(@RequestBody Client clientToAdd);

    @RequestMapping(value = "/api/clients/update/{id}", method = RequestMethod.PUT)
    Client updateClient(@RequestBody Client newData, @PathVariable Long id);

    @RequestMapping(value = "/api/clients/delete/{id}", method = RequestMethod.DELETE)
    void deleteClient(@PathVariable Long id);

    @GetMapping("/api/clients/clear")
    void clearAllData();
}
