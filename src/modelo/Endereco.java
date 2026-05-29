package modelo;

import sistema.TextoUtil;

public class Endereco extends Entidade {
    private String codigoPessoa;
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private TipoEndereco tipoEndereco;

    public Endereco(String codigo, String codigoPessoa, String cep, String logradouro,
                    String numero, String complemento, TipoEndereco tipoEndereco) {
        super(codigo);
        this.codigoPessoa = codigoPessoa;
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.tipoEndereco = tipoEndereco;
    }

    public String getCodigoPessoa() {
        return codigoPessoa;
    }

    public void setCodigoPessoa(String codigoPessoa) {
        this.codigoPessoa = codigoPessoa;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public TipoEndereco getTipoEndereco() {
        return tipoEndereco;
    }

    public void setTipoEndereco(TipoEndereco tipoEndereco) {
        this.tipoEndereco = tipoEndereco;
    }

    @Override
    public String toLinhaArquivo() {
        return TextoUtil.limpar(codigo) + ";" + TextoUtil.limpar(codigoPessoa) + ";" +
               TextoUtil.limpar(cep) + ";" + TextoUtil.limpar(logradouro) + ";" +
               TextoUtil.limpar(numero) + ";" + TextoUtil.limpar(complemento) + ";" +
               tipoEndereco.name();
    }

    public static Endereco fromLinhaArquivo(String linha) {
        String[] partes = linha.split(";", -1);
        if (partes.length < 7) {
            return null;
        }
        try {
            return new Endereco(partes[0], partes[1], partes[2], partes[3], partes[4], partes[5],
                    TipoEndereco.valueOf(partes[6]));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Código: " + codigo + " | Pessoa: " + codigoPessoa + " | CEP: " + cep +
               " | Endereço: " + logradouro + ", " + numero +
               " | Complemento: " + complemento + " | Tipo: " + tipoEndereco;
    }
}
