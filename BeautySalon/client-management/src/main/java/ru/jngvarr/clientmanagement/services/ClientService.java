package ru.jngvarr.clientmanagement.services;

import dao.entities.people.Client;
import exceptions.NotEnoughData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.jngvarr.clientmanagement.repositories.ClientsRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientsRepository clientsRepository;

    public List<Client> getClients() {
        return clientsRepository.findAll();
    }

    public Client getClient(Long id) {
        Optional<Client> neededClient = clientsRepository.findById(id);
        if (neededClient.isPresent()) return neededClient.get();
        else throw new IllegalArgumentException("Client not found");
    }

    public Client getClientByContact(String contact) {
//        List<Client> clients = showAll();
//        for (Client c : clients) {
//            if (c.getContact().equals(phoneNumber)) return c;
//        }
//        throw new IllegalArgumentException("Client not found");
        Client neededClient = clientsRepository.findByContact(contact);
        if (neededClient != null) return neededClient;
        else throw new IllegalArgumentException("Client not found");
    }

    public Client addClient(Client clientToAdd) {
        if ((clientToAdd.getFirstName() != null && clientToAdd.getContact() != null)) {
            return clientsRepository.save(clientToAdd);
        } else throw new NotEnoughData("Not enough client data");
    }

    public Client update(Client newData, Long id) {
        Optional<Client> oldClient = clientsRepository.findById(id);
        if (oldClient.isPresent()) {
            Client newClient = oldClient.get();
            if (newData.getDob() != null) newClient.setDob(newData.getDob());
            if (newData.getContact() != null) newClient.setContact(newData.getContact());
            if (newData.getFirstName() != null) newClient.setFirstName(newData.getFirstName());
            if (newData.getLastName() != null) newClient.setLastName(newData.getLastName());
            return clientsRepository.save(newClient);
        } else throw new IllegalArgumentException("Client not found");
    }

    public void deleteClient(Long id) {
        clientsRepository.deleteById(id);
    }


    public void clearAllData() {
        clientsRepository.deleteAll();
    }


    public List<Client> getClientByName(String name, String lastName) {
        List<Client> clients = clientsRepository.findAll();
        clients = clients.stream().filter(client -> client.getFirstName().equals(name))
                .filter(client -> client.getLastName().equals(lastName)).collect(Collectors.toList());
        Set<Client> set = new HashSet<>(clients);
        clients.clear();
        clients.addAll(set);
        return clients;
    }
}

