package leonardosaes.gerenciador_de_tarefas.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import leonardosaes.gerenciador_de_tarefas.Enum.tagEnum;
import leonardosaes.gerenciador_de_tarefas.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_tarefa")
public class Task{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titulo;

    @NotBlank
    private String descricao;

    @NotBlank
    private String statusTarefa;

    @Enumerated(EnumType.STRING)
    private tagEnum tagEnum;

    @NotNull
    private LocalDate prazoFinal;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task tarefa = (Task) o;
        return Objects.equals(id, tarefa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
