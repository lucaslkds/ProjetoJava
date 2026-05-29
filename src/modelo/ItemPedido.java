package modelo;

public class ItemPedido {
    private Produto produto;
    private int quantidade;
    private double precoUnitario;

    public ItemPedido(Produto produto, int quantidade, double precoUnitario) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public double calcularSubtotal() {
        return quantidade * precoUnitario;
    }

    public String toLinhaArquivo() {
        return produto.getCodigo() + "," + quantidade + "," + precoUnitario;
    }

    @Override
    public String toString() {
        return produto.getCodigo() + " - " + produto.getDescricao() +
               " | Qtd: " + quantidade +
               " | Unitário: R$ " + String.format("%.2f", precoUnitario) +
               " | Subtotal: R$ " + String.format("%.2f", calcularSubtotal());
    }
}
