// AUTOR: CAIO TORRES SEARES

package adocaopets.model.domain;

public enum StatusPetEnum {
    DISPONIVEL("Disponível"),
    INDISPONIVEL("Indisponível"),
    ADOTADO("Adotado");

    private final String descricao;

    StatusPetEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
