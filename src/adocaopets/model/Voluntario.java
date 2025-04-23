package adocaopets.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Voluntario {
    private Usuario usuario;
    private List<FuncaoVoluntario> funcoes = new ArrayList<>();
    private LocalDate data;

    public Voluntario(Usuario usuario, LocalDate data) {
        this.usuario = usuario;
        this.data = data;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<FuncaoVoluntario> getFuncoes() {
        return funcoes;
    }

    public void setFuncoes(List<FuncaoVoluntario> funcoes) {
        this.funcoes = funcoes;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}

