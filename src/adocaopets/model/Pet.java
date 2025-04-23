package adocaopets.model;

public class Pet {
    private int id;
    private String nome;
    private String especie;
    private String raca;
    private String status; // "disponível" ou "indisponível"

    public Pet(int id, String nome, String especie, String raca, String status) {
        this.id = id;
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.status = status;
    }

    // Getters e Setters

}
