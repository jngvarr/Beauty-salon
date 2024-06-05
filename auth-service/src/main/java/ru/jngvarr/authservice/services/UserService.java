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
import security.UserDetailsServiceImpl;
import security.repositories.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final StaffFeignClient staffFeignClient;
    private final UserDetailsServiceImpl userDetailsService;

    public boolean getClientByUserContact(User user) {
        Employee employee = staffFeignClient.getEmployeeByPhone(user.getContact());
        return employee != null && employee.getContact().equals(user.getContact());
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Transactional
    public User createUser(User user) {
//        if (userRepository.findAll().stream()
//                .noneMatch(u -> u.getUsername().equals(user.getUsername()) || u.getEmail().equals(user.getEmail()))) {
        boolean userExists = userRepository.existsByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (!userExists) {
            if (authorityRepository.count() == 0) {
                authorityRepository.save(new Authority("ROLE_USER"));
            }
            user.setAuthorities(authorityRepository.getAuthorityByName("ROLE_USER"));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        } else throw new RuntimeException("Such user already exists");
    }

    public User updateUserToken(User user) {
        User updatedUser = userRepository.getReferenceById(user.getId());
        updatedUser.setTokens(user.getTokens());
        userRepository.save(updatedUser);
        return updatedUser;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<Authority> getAuthorities() {
        return authorityRepository.findAll();
    }

    public void logoutByUsername(String username) {
        User toLogout = userDetailsService.loadUserByUsername(username);
        toLogout.getTokens().clear();
        userRepository.save(toLogout);
    }
}

