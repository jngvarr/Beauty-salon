package ru.jngvarr.authservice.services;

import dao.entities.people.Employee;
import dao.entities.people.User;
import feign_clients.StaffFeignClient;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import security.repositories.UserRepository;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StaffFeignClient staffFeignClient;

    public boolean getClientByUserContact(User user) {
        Employee employee = staffFeignClient.getEmployeeByPhone(user.getContact());
        return employee != null && employee.getContact().equals(user.getContact());
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // if(isUserIsManager(user))user.get   //TODO дописать добавление authorities
        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

}

