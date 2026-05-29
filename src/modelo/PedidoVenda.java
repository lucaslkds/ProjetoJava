package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoVenda extends Entidade {
    private String codigoCliente;
    private String codigoEnderecoEntrega;
    private List<ItemPedido> itens;

    public PedidoVenda(String numeroPedido, String codigoCliente, String codigoEnderecoEntrega) {
        super(numeroPedido);
        this.codigoCliente = codigoCliente;
        this.codigoEnderecoEntrega = codigoEnderecoEntrega;
        this.itens = new ArrayList<>();
    }

    public String getNumeroPedido() {
        return codigo;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public String getCodigoEnderecoEntrega() {
        return codigoEnderecoEntrega;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.calcularSubtotal();
        }
        return total;
    }

    @Override
    public String toLinhaArquivo() {
        String itensTexto = itens.stream()
                .map(ItemPedido::toLinhaArquivo)
                .collect(Collectors.joining("|"));
        return codigo + ";" + codigoCliente + ";" + codigoEnderecoEntrega + ";" + itensTexto + ";" + calcularTotal();
    }

    @Override
    public String toString() {
        return "Pedido: " + codigo + " | Cliente: " + codigoCliente +
               " | Endereço entrega: " + codigoEnderecoEntrega +
               " | Total: R$ " + String.format("%.2f", calcularTotal());
    }
}
