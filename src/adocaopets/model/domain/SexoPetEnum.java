// AUTOR: CAIO TORRES SEARES

package adocaopets.model.domain;

public enum SexoPetEnum {
    MASCULINO('M'),
    FEMININO('F');
    
    private final char valor;
    
    SexoPetEnum(char valor) {
        this.valor = valor;
    }
    
    public char getValor() {
        return valor;
    }
    
    public static SexoPetEnum fromChar(char c) {
        for (SexoPetEnum sexo : values()) {
            if (sexo.valor == c) {
                return sexo;
            }
        }
        throw new IllegalArgumentException("Sexo inválido: " + c);
    }
}
