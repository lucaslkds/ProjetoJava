package modelo;

public enum TipoPessoa {
    CLIENTE,
    FORNECEDOR,
    AMBOS;

    public boolean podeComprar() {
        return this == CLIENTE || this == AMBOS;
    }

    public boolean podeFornecer() {
        return this == FORNECEDOR || this == AMBOS;
    }
}
