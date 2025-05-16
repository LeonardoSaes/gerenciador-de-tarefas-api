package leonardosaes.gerenciador_de_tarefas.repositories;

import leonardosaes.gerenciador_de_tarefas.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
