// ALUNA: GABRIELA BENEVIDES PEREIRA MARQUES
package adocaopets.model.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Voluntario {
    private int id;
    private Usuario usuario;
    private LocalDate dataCadastro;
    private boolean ativo;
    private List<FuncaoVoluntario> funcoes = new ArrayList<>();
    
    
    public Voluntario() {}

    public Voluntario(Usuario usuario) {
        this.usuario = usuario;
        this.dataCadastro = LocalDate.now();
        this.ativo = true;
    }

    public Voluntario(Usuario usuario, FuncaoVoluntario funcao) {
        this.usuario = usuario;
        this.dataCadastro = LocalDate.now();
        this.funcoes.add(funcao);
        this.ativo = true;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate data) {
        this.dataCadastro = data;
    }

    public boolean isAtivo() {
        return ativo == true;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}

