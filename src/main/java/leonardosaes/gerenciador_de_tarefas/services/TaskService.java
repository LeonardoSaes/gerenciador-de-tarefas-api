package leonardosaes.gerenciador_de_tarefas.services;

import leonardosaes.gerenciador_de_tarefas.domain.user.User;
import leonardosaes.gerenciador_de_tarefas.entities.Task;
import leonardosaes.gerenciador_de_tarefas.repositories.TaskRepository;
import leonardosaes.gerenciador_de_tarefas.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    public TaskRepository repository;

    @Autowired
    public UserRepository userRepository;


    public List<Task> listaTodasTarefas(String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return repository.findByUsuario(usuario);
    }

    public List<Task> listarTarefasDoUsuario(String email) {
        User usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return repository.findByUsuario(usuario);
    }

    public Task buscaPorId(Long id){
        Optional<Task> tarefa = repository.findById(id);
        return tarefa.stream()
                .findAny()
                .orElse(null);
    }

    public List<Task> buscaPorTitulo(String titulo){
        return repository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Task> buscaPorDescricao(String descricao){
        return repository.findByDescricaoContainingIgnoreCase(descricao);
    }

    public Task salvaTarefa(Task tarefa){
        return repository.save(tarefa);
    }

    public Task atualizaTarefa(Long id, Task tarefaAtualizada){
        if (tarefaAtualizada == null) {
            return null;
        }

        return repository.findById(id).map(tarefa -> {
            tarefa.setTitulo(tarefaAtualizada.getTitulo());
            tarefa.setDescricao(tarefaAtualizada.getDescricao());
            tarefa.setStatusTarefa(tarefaAtualizada.getStatusTarefa());
            tarefa.setTagEnum(tarefaAtualizada.getTagEnum());
            tarefa.setPrazoFinal(tarefaAtualizada.getPrazoFinal());
            return repository.save(tarefa);
        }).orElse(null);
    }

    public void deletaFilme(Long id){
        repository.deleteById(id);
    }
}
