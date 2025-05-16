package leonardosaes.gerenciador_de_tarefas.dto;

public record LoginRequestDTO(String email, String password) {

    @Override
    public String password() {
        return password;
    }

    @Override
    public String email() {
        return email;
    }
}
