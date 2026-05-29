package modelo;

public abstract class Entidade {
    protected String codigo;

    public Entidade(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public abstract String toLinhaArquivo();
}
