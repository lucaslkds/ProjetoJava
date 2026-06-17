package modelo;

/**
 * Factory Method para centralizar a criação de pessoas do sistema.
 *
 * Em vez de espalhar "new Pessoa(...)" pelos cadastros, usamos esta fábrica
 * para deixar a criação de CLIENTE, FORNECEDOR e AMBOS em um único lugar.
 */
public class PessoaFactory {

    private PessoaFactory() {
        // Evita instanciar a fábrica, pois os métodos são estáticos.
    }

    public static Pessoa criarPorTipo(String codigo, String nome, TipoPessoa tipoPessoa) {
        switch (tipoPessoa) {
            case CLIENTE:
                return criarCliente(codigo, nome);
            case FORNECEDOR:
                return criarFornecedor(codigo, nome);
            case AMBOS:
                return criarAmbos(codigo, nome);
            default:
                throw new IllegalArgumentException("Tipo de pessoa inválido.");
        }
    }

    public static Pessoa criarCliente(String codigo, String nome) {
        return new Pessoa(codigo, nome, TipoPessoa.CLIENTE);
    }

    public static Pessoa criarFornecedor(String codigo, String nome) {
        return new Pessoa(codigo, nome, TipoPessoa.FORNECEDOR);
    }

    public static Pessoa criarAmbos(String codigo, String nome) {
        return new Pessoa(codigo, nome, TipoPessoa.AMBOS);
    }
}
