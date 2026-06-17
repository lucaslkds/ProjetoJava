package modelo;

import sistema.TextoUtil;

public class Endereco extends Entidade {
    private String codigoPessoa;
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private TipoEndereco tipoEndereco;

    /**
     * Construtor privado para forçar a criação via Builder.
     * Isso deixa mais clara a montagem de um objeto que possui muitos campos.
     */
    private Endereco(Builder builder) {
        super(builder.codigo);
        this.codigoPessoa = builder.codigoPessoa;
        this.cep = builder.cep;
        this.logradouro = builder.logradouro;
        this.numero = builder.numero;
        this.complemento = builder.complemento;
        this.tipoEndereco = builder.tipoEndereco;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String codigo;
        private String codigoPessoa;
        private String cep;
        private String logradouro;
        private String numero;
        private String complemento;
        private TipoEndereco tipoEndereco;

        public Builder codigo(String codigo) {
            this.codigo = codigo;
            return this;
        }

        public Builder codigoPessoa(String codigoPessoa) {
            this.codigoPessoa = codigoPessoa;
            return this;
        }

        public Builder cep(String cep) {
            this.cep = cep;
            return this;
        }

        public Builder logradouro(String logradouro) {
            this.logradouro = logradouro;
            return this;
        }

        public Builder numero(String numero) {
            this.numero = numero;
            return this;
        }

        public Builder complemento(String complemento) {
            this.complemento = complemento;
            return this;
        }

        public Builder tipoEndereco(TipoEndereco tipoEndereco) {
            this.tipoEndereco = tipoEndereco;
            return this;
        }

        public Endereco build() {
            return new Endereco(this);
        }
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
            return Endereco.builder()
                    .codigo(partes[0])
                    .codigoPessoa(partes[1])
                    .cep(partes[2])
                    .logradouro(partes[3])
                    .numero(partes[4])
                    .complemento(partes[5])
                    .tipoEndereco(TipoEndereco.valueOf(partes[6]))
                    .build();
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
