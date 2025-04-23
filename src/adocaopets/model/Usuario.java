package adocaopets.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int id;
    private String nome;
    private List<Pet> petsAdotados = new ArrayList<>();

    public Usuario(int id, String nome) {
        this.id = id;
        this.nome = nome;
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

    public List<Pet> getPetsAdotados() {
        return petsAdotados;
    }

    public void setPetsAdotados(List<Pet> petsAdotados) {
        this.petsAdotados = petsAdotados;
    }
}
