package leonardosaes.gerenciador_de_tarefas.Enum;

public enum StatusTaskEnum {
    CRIADA(1, "Criada"),
    EM_PROGRESSO(2, "Em Progresso"),
    FINALIZADA(3, "Finalizada");

    private int id;
    private String descricao;

    StatusTaskEnum(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
