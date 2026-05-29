package cadastro;

import modelo.Pessoa;
import modelo.Produto;
import sistema.ArquivoService;
import sistema.EntradaService;
import sistema.LogService;
import sistema.MenuService;
import sistema.OpcaoMenu;
import sistema.TextoUtil;

import java.util.ArrayList;
import java.util.List;

public class ProdutoCadastro {
    public static final String ARQUIVO = "dados/produtos.txt";

    public void executarMenu() {
        int opcao;
        do {
            OpcaoMenu escolhida = MenuService.montarMenu("menus/menu-produtos.txt");
            if (escolhida == null) {
                return;
            }
            opcao = escolhida.getOpcao();

            switch (opcao) {
                case 1:
                    incluir();
                    break;
                case 2:
                    consultar();
                    break;
                case 3:
                    alterar();
                    break;
                case 4:
                    excluir();
                    break;
                case 5:
                    listar();
                    break;
                case 0:
                    System.out.println("Voltando ao menu principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }

            if (opcao != 0) {
                EntradaService.aguardarEnter();
            }
        } while (opcao != 0);
    }

    public void incluir() {
        System.out.println("\n=== Incluir Produto ===");
        String codigo = EntradaService.lerTextoObrigatorio("Código do produto: ");
        if (buscarPorCodigo(codigo) != null) {
            System.out.println("Já existe produto cadastrado com esse código.");
            return;
        }

        String descricao = EntradaService.lerTextoObrigatorio("Descrição: ");
        double custo = EntradaService.lerDoubleNaoNegativo("Custo R$: ");
        double precoVenda = lerPrecoVenda(custo);
        String codigoFornecedor = lerFornecedorValido();

        Produto produto = new Produto(codigo, descricao, custo, precoVenda, codigoFornecedor);
        ArquivoService.adicionarLinha(ARQUIVO, produto.toLinhaArquivo());
        LogService.registrar("INCLUSAO", "PRODUTO", codigo);
        System.out.println("Produto cadastrado com sucesso.");
    }

    public void consultar() {
        System.out.println("\n=== Consultar Produto ===");
        String termo = EntradaService.lerTextoObrigatorio("Digite código, descrição ou fornecedor: ").toLowerCase();
        boolean encontrou = false;

        for (Produto produto : listarObjetos()) {
            if (produto.getCodigo().toLowerCase().contains(termo)
                    || produto.getDescricao().toLowerCase().contains(termo)
                    || produto.getCodigoFornecedor().toLowerCase().contains(termo)) {
                System.out.println(produto);
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhum produto encontrado.");
        }
    }

    public void alterar() {
        System.out.println("\n=== Alterar Produto ===");
        String codigo = EntradaService.lerTextoObrigatorio("Código do produto: ");
        List<Produto> produtos = listarObjetos();

        for (int i = 0; i < produtos.size(); i++) {
            Produto atual = produtos.get(i);
            if (atual.getCodigo().equals(codigo)) {
                System.out.println("Registro atual: " + atual);

                String descricao = EntradaService.lerTexto("Nova descrição (ENTER mantém): ");
                if (!TextoUtil.vazio(descricao)) {
                    atual.setDescricao(descricao);
                }

                String alterarCusto = EntradaService.lerTexto("Alterar custo? (S/N): ");
                if (alterarCusto.equalsIgnoreCase("S")) {
                    atual.setCusto(EntradaService.lerDoubleNaoNegativo("Novo custo R$: "));
                }

                String alterarPreco = EntradaService.lerTexto("Alterar preço de venda? (S/N): ");
                if (alterarPreco.equalsIgnoreCase("S")) {
                    atual.setPrecoVenda(lerPrecoVenda(atual.getCusto()));
                }

                String alterarFornecedor = EntradaService.lerTexto("Alterar fornecedor? (S/N): ");
                if (alterarFornecedor.equalsIgnoreCase("S")) {
                    atual.setCodigoFornecedor(lerFornecedorValido());
                }

                produtos.set(i, atual);
                salvarObjetos(produtos);
                LogService.registrar("ALTERACAO", "PRODUTO", codigo);
                System.out.println("Produto alterado com sucesso.");
                return;
            }
        }

        System.out.println("Produto não encontrado.");
    }

    public void excluir() {
        System.out.println("\n=== Excluir Produto ===");
        String codigo = EntradaService.lerTextoObrigatorio("Código do produto: ");

        if (produtoEmPedido(codigo)) {
            System.out.println("Não é possível excluir. Esse produto está vinculado a pedido.");
            return;
        }

        List<Produto> produtos = listarObjetos();
        boolean removeu = produtos.removeIf(p -> p.getCodigo().equals(codigo));

        if (removeu) {
            salvarObjetos(produtos);
            LogService.registrar("EXCLUSAO", "PRODUTO", codigo);
            System.out.println("Produto excluído com sucesso.");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    public void listar() {
        System.out.println("\n=== Lista de Produtos ===");
        List<Produto> produtos = listarObjetos();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }
        for (Produto produto : produtos) {
            System.out.println(produto);
        }
    }

    public static Produto buscarPorCodigo(String codigo) {
        for (Produto produto : listarObjetos()) {
            if (produto.getCodigo().equals(codigo)) {
                return produto;
            }
        }
        return null;
    }

    public static List<Produto> listarObjetos() {
        List<Produto> produtos = new ArrayList<>();
        for (String linha : ArquivoService.lerLinhas(ARQUIVO)) {
            Produto produto = Produto.fromLinhaArquivo(linha);
            if (produto != null) {
                produtos.add(produto);
            }
        }
        return produtos;
    }

    private static void salvarObjetos(List<Produto> produtos) {
        List<String> linhas = new ArrayList<>();
        for (Produto produto : produtos) {
            linhas.add(produto.toLinhaArquivo());
        }
        ArquivoService.salvarLinhas(ARQUIVO, linhas);
    }

    private double lerPrecoVenda(double custo) {
        while (true) {
            double precoVenda = EntradaService.lerDoubleNaoNegativo("Preço de venda R$: ");
            if (precoVenda >= custo) {
                return precoVenda;
            }
            System.out.println("Preço de venda não pode ser menor que o custo.");
        }
    }

    private String lerFornecedorValido() {
        while (true) {
            String codigoFornecedor = EntradaService.lerTextoObrigatorio("Código do fornecedor: ");
            Pessoa fornecedor = PessoaCadastro.buscarPorCodigo(codigoFornecedor);
            if (fornecedor == null) {
                System.out.println("Fornecedor não encontrado no cadastro de pessoas.");
                continue;
            }
            if (!fornecedor.getTipoPessoa().podeFornecer()) {
                System.out.println("A pessoa informada não é FORNECEDOR nem AMBOS.");
                continue;
            }
            return codigoFornecedor;
        }
    }

    private boolean produtoEmPedido(String codigoProduto) {
        for (String linha : ArquivoService.lerLinhas(PedidoCadastro.ARQUIVO)) {
            String[] partes = linha.split(";", -1);
            if (partes.length > 3) {
                String[] itens = partes[3].split("\\|");
                for (String item : itens) {
                    String[] campos = item.split(",", -1);
                    if (campos.length > 0 && campos[0].equals(codigoProduto)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
