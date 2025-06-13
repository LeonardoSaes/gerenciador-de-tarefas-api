package leonardosaes.gerenciador_de_tarefas.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import leonardosaes.gerenciador_de_tarefas.domain.user.User;
import leonardosaes.gerenciador_de_tarefas.entities.Task;
import leonardosaes.gerenciador_de_tarefas.repositories.TaskRepository;
import leonardosaes.gerenciador_de_tarefas.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/tarefa")
@Tag(name = "Tarefas", description = "Operações relacionadas às tarefas dos usuários")
public class TaskController {

    @Autowired
    public TaskService taskService;

    @Autowired
    public TaskRepository taskRepository;

    @Operation(summary = "Cadastrar nova tarefa", description = "Cria uma nova tarefa associada ao usuário autenticado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro na validação da tarefa"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/cadastrar")
    @Transactional // Garante que a operação ocorra dentro de uma transação Hibernate
    public ResponseEntity<Task> criaTarefa(@RequestBody @Valid Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        task.setUsuario(user); // Define o relacionamento com o usuário

        taskRepository.save(task);

        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }
    @Operation(summary = "Listar todas as tarefas", description = "Retorna todas as tarefas do usuário autenticado.")
    @GetMapping(value = "/listar")
    public ResponseEntity<List<Task>> listaTodasTarefas(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            String email = userDetails.getUsername();
            List<Task> listaDeTarefas = taskService.listaTodasTarefas(email);

            if (listaDeTarefas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.ok().body(listaDeTarefas);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Atualizar tarefa", description = "Atualiza os dados de uma tarefa existente pelo ID.")
    @PatchMapping(value = "/{id}")
    public ResponseEntity<Task> atualizaTarefa(@PathVariable Long id, @RequestBody @Valid Task tarefaAtualizada){
        try{
            Task novaTarefa = taskService.atualizaTarefa(id, tarefaAtualizada);
            return novaTarefa != null ? ResponseEntity.ok().body(tarefaAtualizada) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Deletar tarefa", description = "Remove uma tarefa pelo ID.")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletaTarefa(@PathVariable Long id){
            try {

                Optional<Task> tarefaOptional = Optional.ofNullable(taskService.buscaPorId(id));

                if (tarefaOptional.isPresent()) {
                    taskService.deletaFilme(id);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // Retorna 500 em caso de erro
        }
    }
    @Operation(summary = "Buscar tarefa por ID", description = "Retorna uma tarefa específica pelo seu ID.")
    @GetMapping(value = "/listar/{id}")
    public ResponseEntity<Task> buscaPorId(@PathVariable Long id){
        try{
            Task tarefa = taskService.buscaPorId(id);
            if(tarefa != null){
                return ResponseEntity.ok().body(tarefa);
            } else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Buscar tarefas por título", description = "Busca todas as tarefas que contenham o título informado.")
    @GetMapping(value = "/buscar-por-titulo")
    public ResponseEntity<List<Task>> buscaPorTitulo(@RequestParam String titulo) {

        try {
            List<Task> tarefas = taskService.buscaPorTitulo(titulo);

            if (tarefas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(tarefas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @Operation(summary = "Buscar tarefas por descrição", description = "Busca todas as tarefas que contenham a descrição informada.")
    @GetMapping(value = "/busca-por-descricao")
    public ResponseEntity<List<Task>> buscaPorDescricao(@RequestParam String descricao) {
        try {
            List<Task> tarefas = taskService.buscaPorDescricao(descricao);

            if (tarefas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok(tarefas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
