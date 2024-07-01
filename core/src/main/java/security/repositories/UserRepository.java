package security.repositories;

import dao.entities.people.SalonUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<SalonUser, Long> {
    SalonUser getUserByEmail(String email);

    boolean existsByUsernameOrEmail(String username, String email);

    Optional<SalonUser> getUserByUsername(String username);
}
