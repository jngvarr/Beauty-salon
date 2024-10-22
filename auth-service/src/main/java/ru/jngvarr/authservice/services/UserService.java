package ru.jngvarr.authservice.services;

import dao.entities.Authority;
import dao.entities.people.Employee;
import dao.entities.people.SalonUser;
import feign_clients.StaffFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.jngvarr.authservice.repositories.AuthorityRepository;
import security.UserDetailsServiceImpl;
import security.repositories.UserRepository;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final StaffFeignClient staffFeignClient;
    private final UserDetailsServiceImpl userDetailsService;

    public boolean getClientByUserContact(SalonUser salonUser) {
        Employee employee = staffFeignClient.getEmployeeByPhone(salonUser.getContact());
        return employee != null && employee.getContact().equals(salonUser.getContact());
    }

    public SalonUser getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Transactional
    public SalonUser createUser(SalonUser salonUser) {
//        if (userRepository.findAll().stream()
//                .noneMatch(u -> u.getUsername().equals(user.getUsername()) || u.getEmail().equals(user.getEmail()))) {
        boolean userExists = userRepository.existsByUsernameOrEmail(salonUser.getUsername(), salonUser.getEmail());
        if (authorityRepository.count() == 0) {
            initialize();
        }
        if (!userExists) {
            salonUser.setAuthorities(authorityRepository.getAuthorityByName("ROLE_USER"));
            salonUser.setPassword(passwordEncoder.encode(salonUser.getPassword()));
            return userRepository.save(salonUser);
        } else throw new

                RuntimeException("Such user already exists");
    }

    public void initialize() {
        authorityRepository.save(new Authority("ROLE_ADMIN"));
        authorityRepository.save(new Authority("ROLE_USER"));
        SalonUser admin = new SalonUser();
        admin.setAuthorities(authorityRepository.getAuthorityByName("ROLE_ADMIN"));
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setContact("set contact");
        admin.setEmail("set email");
        userRepository.save(admin);
    }

    public SalonUser updateUserToken(SalonUser salonUser) {
        SalonUser updatedSalonUser = userRepository.getReferenceById(salonUser.getId());
        updatedSalonUser.setTokens(salonUser.getTokens());
        userRepository.save(updatedSalonUser);
        return updatedSalonUser;
    }

    public List<SalonUser> getUsers() {
        return userRepository.findAll();
    }

    public List<Authority> getAuthorities() {
        return authorityRepository.findAll();
    }

    public void logoutByUsername(String username) {
        SalonUser toLogout = userDetailsService.loadUserByUsername(username);
        toLogout.getTokens().clear();
        userRepository.save(toLogout);
    }
}

