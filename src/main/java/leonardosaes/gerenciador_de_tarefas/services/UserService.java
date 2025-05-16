package leonardosaes.gerenciador_de_tarefas.services;

import leonardosaes.gerenciador_de_tarefas.domain.user.User;
import leonardosaes.gerenciador_de_tarefas.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    public UserRepository repository;

    List<User> listaTodosUsuarios(){
        return repository.findAll();
    }

    public User buscaPorId(Long id){
        Optional<User> usuario = repository.findById(id);
        return usuario.get();
    }

    public User salvaUsuario(User usuario){
        return repository.save(usuario);
    }

    public void deletaUsuario(Long id){
        repository.deleteById(id);
    }

}
