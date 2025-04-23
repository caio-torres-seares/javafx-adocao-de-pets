package adocaopets.model;

public class Pet {
    private int id;
    private String nome;
    private String especie;
    private String raca;
    private String status; // "disponível" ou "indisponível"
    // adicionando comentário teste

    public Pet(int id, String nome, String especie, String raca, String status) {
        this.id = id;
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
