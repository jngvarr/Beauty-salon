package security.repositories;

import dao.entities.people.salonUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<salonUser, Long> {
    salonUser getUserByEmail(String email);

    boolean existsByUsernameOrEmail(String username, String email);

    Optional<salonUser> getUserByUsername(String username);
}
