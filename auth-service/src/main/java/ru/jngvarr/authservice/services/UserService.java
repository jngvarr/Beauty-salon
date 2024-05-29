package ru.jngvarr.authservice.services;

import dao.entities.Authority;
import dao.entities.people.Employee;
import dao.entities.people.User;
import feign_clients.StaffFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jngvarr.authservice.repositories.AuthorityRepository;
import security.repositories.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final StaffFeignClient staffFeignClient;

    public boolean getClientByUserContact(User user) {
        Employee employee = staffFeignClient.getEmployeeByPhone(user.getContact());
        return employee != null && employee.getContact().equals(user.getContact());
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Transactional
    public User createUser(User user) {
        // Преобразование Collection<? extends GrantedAuthority> в List<String>
        List<String> userAuthorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // Получение имен всех существующих авторитетов из базы данных
        List<String> existingAuthoritiesNames = getAuthorities().stream()
                .map(Authority::getName)
                .toList();

        // Сохранение новых авторитетов
        userAuthorities.stream()
                .filter(authority -> !existingAuthoritiesNames.contains(authority))
                .forEach(authority -> {
                    Authority auth = new Authority();
                    auth.setName(authority);
                    authorityRepository.save(auth);
                });
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<Authority> getAuthorities() {
        return authorityRepository.findAll();
    }

}

