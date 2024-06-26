package ru.jngvarr.servicemanagement.services;

import dao.entities.Servize;
import exceptions.NeededObjectNotFound;
import exceptions.NotEnoughData;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.jngvarr.servicemanagement.repositories.SalonServiceRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceForServices {
    private final SalonServiceRepository salonServiceRepository;

    public List<Servize> getServices() {
        return salonServiceRepository.findAll();
    }

    public Servize getService(Long id) {
        Optional<Servize> neededService = salonServiceRepository.findById(id);
        return neededService.orElseThrow(() -> new NeededObjectNotFound("Service not found: " + id));
    }

    public Servize addService(Servize newService) {
        if (newService.getTitle() != null && newService.getServiceDurationInMinutes() != null && newService.getPrice() != null)
            return salonServiceRepository.save(newService);
        else throw new NotEnoughData("Not enough service data");
    }

    public Servize update(Servize newData, Long id) {
        Optional<Servize> oldService = salonServiceRepository.findById(id);
        if (oldService.isPresent()) {
            Servize newServize = oldService.get();
            if (newData.getPrice() != null) newServize.setPrice(newData.getPrice());
            if (newData.getTitle() != null) newServize.setTitle(newData.getTitle());
            if (newData.getServiceDurationInMinutes() != null)
                newServize.setServiceDurationInMinutes(newData.getServiceDurationInMinutes());
            if (newData.getConsumables() != null) newServize.setConsumables(newData.getConsumables());
            if (newData.getDescription() != null) newServize.setDescription(newData.getDescription());
            return salonServiceRepository.save(newServize);
        } else throw new IllegalArgumentException("Service not found");
    }

    public int getServiceDuration(Long id) {
        Optional<Servize> neededService = salonServiceRepository.findById(id);
        if (neededService.isPresent()) return neededService.get().getServiceDurationInMinutes();
        else throw new RuntimeException("Service is absent!");
    }

    public List<Servize> getServicesByTitle(String title) {
        List<Servize> neededServices = salonServiceRepository.findAllByTitle(title);
        if (!neededServices.isEmpty()) return neededServices;
        else throw new RuntimeException("Service is absent!");
    }

    public List<Servize> getServicesByDescription(String description) {
        List<Servize> neededServices = salonServiceRepository.findAllByDescriptionContainingIgnoreCase(description);
        if (!neededServices.isEmpty()) return neededServices;
        else throw new RuntimeException("Service is absent!");
    }

    public void delete(Long id) {
        salonServiceRepository.deleteById(id);
    }

    public List<Servize> getServicesByConsumable(String consumable) {
        List<Servize> neededServices = salonServiceRepository.findByConsumableTitleContaining(consumable);
        if (!neededServices.isEmpty()) return neededServices;
        else throw new RuntimeException("Service is absent!");
    }
}
