// ALUNA: GABRIELA BENEVIDES PEREIRA MARQUES

package adocaopets.model.domain;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int id;
    private String nome;
    private String cpf;
    private List<Pet> petsAdotados = new ArrayList<>();
    private boolean voluntario;

    public Usuario(){}
    
    public Usuario(int id, String nome, String cpf) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<Pet> getPetsAdotados() {
        return petsAdotados;
    }

    public void setPetsAdotados(List<Pet> petsAdotados) {
        this.petsAdotados = petsAdotados;
    }

    public boolean isVoluntario() {
        return voluntario;
    }

    public void setVoluntario(boolean voluntario) {
        this.voluntario = voluntario;
    }
    
    @Override
    public String toString() {
        return this.nome; 
    }
}
