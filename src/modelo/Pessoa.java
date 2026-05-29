package modelo;

import sistema.TextoUtil;

public class Pessoa extends Entidade {
    private String nome;
    private TipoPessoa tipoPessoa;

    public Pessoa(String codigo, String nome, TipoPessoa tipoPessoa) {
        super(codigo);
        this.nome = nome;
        this.tipoPessoa = tipoPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoPessoa getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(TipoPessoa tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    @Override
    public String toLinhaArquivo() {
        return TextoUtil.limpar(codigo) + ";" + TextoUtil.limpar(nome) + ";" + tipoPessoa.name();
    }

    public static Pessoa fromLinhaArquivo(String linha) {
        String[] partes = linha.split(";", -1);
        if (partes.length < 3) {
            return null;
        }
        try {
            return new Pessoa(partes[0], partes[1], TipoPessoa.valueOf(partes[2]));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Código: " + codigo + " | Nome: " + nome + " | Tipo: " + tipoPessoa;
    }
}
