package security.repositories;

import dao.entities.people.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByEmail(String email);

    boolean existsByUsernameOrEmail(String username, String email);

    User getUserByUsername(String username);
}
