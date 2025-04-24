package adocaopets.model.domain;

import java.util.ArrayList;
import java.util.List;

public class FuncaoVoluntario {
    private String nome;
    private int limiteVoluntarios;
    private List<Usuario> voluntarios = new ArrayList<>();

    public FuncaoVoluntario(String nome, int limiteVoluntarios) {
        this.nome = nome;
        this.limiteVoluntarios = limiteVoluntarios;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getLimiteVoluntarios() {
        return limiteVoluntarios;
    }

    public void setLimiteVoluntarios(int limiteVoluntarios) {
        this.limiteVoluntarios = limiteVoluntarios;
    }

    public List<Usuario> getVoluntarios() {
        return voluntarios;
    }

    public void setVoluntarios(List<Usuario> voluntarios) {
        this.voluntarios = voluntarios;
    }
}
