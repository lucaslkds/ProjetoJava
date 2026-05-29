package sistema;

public class OpcaoMenu {
    private int opcao;
    private String descricao;

    public OpcaoMenu(int opcao, String descricao) {
        this.opcao = opcao;
        this.descricao = descricao;
    }

    public int getOpcao() {
        return opcao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return opcao + " - " + descricao;
    }
}
