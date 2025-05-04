// ALUNOS: CAIO TORRES SEARES e GABRIELA BENEVIDES PEREIRA MARQUES
package adocaopets.model.domain;


public class FuncaoVoluntario {
    private Integer id;
    private String nome;
    private String descricao;
    private Integer limiteVoluntarios;

    public FuncaoVoluntario() {}

    public FuncaoVoluntario(String nome, String descricao, Integer limiteVoluntarios) {
        this.nome = nome;
        this.descricao = descricao;
        this.limiteVoluntarios = limiteVoluntarios;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getLimiteVoluntarios() {
        return limiteVoluntarios;
    }

    public void setLimiteVoluntarios(Integer limiteVoluntarios) {
        this.limiteVoluntarios = limiteVoluntarios;
    }

    @Override
    public String toString() {
        return nome;
    }
}

