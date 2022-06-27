package pl.cs50.network.repostiory;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.cs50.network.model.User.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
