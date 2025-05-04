// AUTOR: CAIO TORRES SEARES

package adocaopets.model.domain;

import java.io.Serializable;

public class Pet implements Serializable{
    private int id;
    private String nome;
    private String especie;
    private String raca;
    private int idade;
    private SexoPetEnum sexo;
    private StatusPetEnum status; // "disponível", "indisponível" ou "adotado"
    
    public Pet(){
    }

    public Pet(String nome, String especie, String raca, int idade, SexoPetEnum sexo) {
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.idade = idade;
        this.sexo = sexo;
        this.status = StatusPetEnum.DISPONIVEL;
    }
    
    public Pet(String nome, String especie, String raca, int idade, SexoPetEnum sexo, StatusPetEnum status) {
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.idade = idade;
        this.sexo = sexo;
        this.status = status;
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

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }
    
    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public SexoPetEnum getSexo() {
        return sexo;
    }

    public void setSexo(SexoPetEnum sexo) {
        this.sexo = sexo;
    }

    public StatusPetEnum getStatus() {
        return status;
    }

    public void setStatus(StatusPetEnum status) {
        this.status = status;
    }
    
    
}
