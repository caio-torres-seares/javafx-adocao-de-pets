// AUTOR: CAIO TORRES SEARES

package adocaopets.model.domain;

public enum SexoPetEnum {
    MASCULINO("M", "Masculino"),
    FEMININO("F", "Feminino");

    private final String valorBanco;
    private final String exibicao;

    SexoPetEnum(String valorBanco, String exibicao) {
        this.valorBanco = valorBanco;
        this.exibicao = exibicao;
    }

    public String getValorBanco() {
        return valorBanco;
    }

    public String getExibicao() {
        return exibicao;
    }

    public static SexoPetEnum fromValorBanco(String valorBanco) {
        for (SexoPetEnum sexo : values()) {
            if (sexo.valorBanco.equalsIgnoreCase(valorBanco)) {
                return sexo;
            }
        }
        throw new IllegalArgumentException("Valor inválido para SexoPetEnum: " + valorBanco);
    }
}
