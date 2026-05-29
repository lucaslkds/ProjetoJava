package modelo;

import sistema.TextoUtil;

public class Produto extends Entidade {
    private String descricao;
    private double custo;
    private double precoVenda;
    private String codigoFornecedor;

    public Produto(String codigo, String descricao, double custo, double precoVenda, String codigoFornecedor) {
        super(codigo);
        this.descricao = descricao;
        this.custo = custo;
        this.precoVenda = precoVenda;
        this.codigoFornecedor = codigoFornecedor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getCusto() {
        return custo;
    }

    public void setCusto(double custo) {
        this.custo = custo;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public String getCodigoFornecedor() {
        return codigoFornecedor;
    }

    public void setCodigoFornecedor(String codigoFornecedor) {
        this.codigoFornecedor = codigoFornecedor;
    }

    @Override
    public String toLinhaArquivo() {
        return TextoUtil.limpar(codigo) + ";" + TextoUtil.limpar(descricao) + ";" +
               custo + ";" + precoVenda + ";" + TextoUtil.limpar(codigoFornecedor);
    }

    public static Produto fromLinhaArquivo(String linha) {
        String[] partes = linha.split(";", -1);
        if (partes.length < 5) {
            return null;
        }
        try {
            return new Produto(partes[0], partes[1], Double.parseDouble(partes[2]),
                    Double.parseDouble(partes[3]), partes[4]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Código: " + codigo + " | Descrição: " + descricao +
               " | Custo: R$ " + String.format("%.2f", custo) +
               " | Venda: R$ " + String.format("%.2f", precoVenda) +
               " | Fornecedor: " + codigoFornecedor;
    }
}
