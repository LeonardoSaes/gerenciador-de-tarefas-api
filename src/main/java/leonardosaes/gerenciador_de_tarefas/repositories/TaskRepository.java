package leonardosaes.gerenciador_de_tarefas.repositories;

import leonardosaes.gerenciador_de_tarefas.domain.user.User;
import leonardosaes.gerenciador_de_tarefas.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByTituloContainingIgnoreCase(String nome);

    List<Task> findByDescricaoContainingIgnoreCase(String descricao);

    List<Task> findByUsuario(User usuario);
}
