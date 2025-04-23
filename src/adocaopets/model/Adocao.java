package adocaopets.model;

import java.time.LocalDate;

public class Adocao {
    private int id;
    private Usuario usuario;
    private Pet pet;
    private LocalDate data;

    public Adocao(int id, Usuario usuario, Pet pet, LocalDate data) {
        this.id = id;
        this.usuario = usuario;
        this.pet = pet;
        this.data = data;
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

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
