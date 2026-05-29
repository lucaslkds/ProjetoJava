package cadastro;

import modelo.Endereco;
import modelo.ItemPedido;
import modelo.PedidoVenda;
import modelo.Pessoa;
import modelo.Produto;
import sistema.ArquivoService;
import sistema.EntradaService;
import sistema.LogService;
import sistema.MenuService;
import sistema.OpcaoMenu;

import java.util.ArrayList;
import java.util.List;

public class PedidoCadastro {
    public static final String ARQUIVO = "dados/pedidos.txt";

    public void executarMenu() {
        int opcao;
        do {
            OpcaoMenu escolhida = MenuService.montarMenu("menus/menu-pedidos.txt");
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
        System.out.println("\n=== Incluir Pedido de Venda ===");
        String numeroPedido = EntradaService.lerTextoObrigatorio("Número do pedido: ");
        if (buscarPorCodigo(numeroPedido) != null) {
            System.out.println("Já existe pedido com esse número.");
            return;
        }

        PedidoVenda pedido = montarPedido(numeroPedido);
        if (pedido == null) {
            return;
        }

        ArquivoService.adicionarLinha(ARQUIVO, pedido.toLinhaArquivo());
        LogService.registrar("INCLUSAO", "PEDIDO", numeroPedido);
        System.out.println("Pedido cadastrado com sucesso. Total: R$ " + String.format("%.2f", pedido.calcularTotal()));
    }

    public void consultar() {
        System.out.println("\n=== Consultar Pedido ===");
        String termo = EntradaService.lerTextoObrigatorio("Digite número do pedido ou código do cliente: ").toLowerCase();
        boolean encontrou = false;

        for (PedidoVenda pedido : listarObjetos()) {
            if (pedido.getCodigo().toLowerCase().contains(termo)
                    || pedido.getCodigoCliente().toLowerCase().contains(termo)) {
                imprimirPedidoCompleto(pedido);
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhum pedido encontrado.");
        }
    }

    public void alterar() {
        System.out.println("\n=== Alterar Pedido ===");
        String numeroPedido = EntradaService.lerTextoObrigatorio("Número do pedido: ");
        List<PedidoVenda> pedidos = listarObjetos();

        for (int i = 0; i < pedidos.size(); i++) {
            if (pedidos.get(i).getCodigo().equals(numeroPedido)) {
                System.out.println("Pedido atual:");
                imprimirPedidoCompleto(pedidos.get(i));
                System.out.println("\nO pedido será reconstruído mantendo o mesmo número.");

                PedidoVenda novoPedido = montarPedido(numeroPedido);
                if (novoPedido == null) {
                    return;
                }

                pedidos.set(i, novoPedido);
                salvarObjetos(pedidos);
                LogService.registrar("ALTERACAO", "PEDIDO", numeroPedido);
                System.out.println("Pedido alterado com sucesso.");
                return;
            }
        }

        System.out.println("Pedido não encontrado.");
    }

    public void excluir() {
        System.out.println("\n=== Excluir Pedido ===");
        String numeroPedido = EntradaService.lerTextoObrigatorio("Número do pedido: ");
        List<PedidoVenda> pedidos = listarObjetos();
        boolean removeu = pedidos.removeIf(p -> p.getCodigo().equals(numeroPedido));

        if (removeu) {
            salvarObjetos(pedidos);
            LogService.registrar("EXCLUSAO", "PEDIDO", numeroPedido);
            System.out.println("Pedido excluído com sucesso.");
        } else {
            System.out.println("Pedido não encontrado.");
        }
    }

    public void listar() {
        System.out.println("\n=== Lista de Pedidos ===");
        List<PedidoVenda> pedidos = listarObjetos();
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido cadastrado.");
            return;
        }
        for (PedidoVenda pedido : pedidos) {
            imprimirPedidoCompleto(pedido);
        }
    }

    public static PedidoVenda buscarPorCodigo(String codigo) {
        for (PedidoVenda pedido : listarObjetos()) {
            if (pedido.getCodigo().equals(codigo)) {
                return pedido;
            }
        }
        return null;
    }

    public static List<PedidoVenda> listarObjetos() {
        List<PedidoVenda> pedidos = new ArrayList<>();
        for (String linha : ArquivoService.lerLinhas(ARQUIVO)) {
            PedidoVenda pedido = fromLinhaArquivo(linha);
            if (pedido != null) {
                pedidos.add(pedido);
            }
        }
        return pedidos;
    }

    private static PedidoVenda fromLinhaArquivo(String linha) {
        String[] partes = linha.split(";", -1);
        if (partes.length < 5) {
            return null;
        }

        PedidoVenda pedido = new PedidoVenda(partes[0], partes[1], partes[2]);
        if (!partes[3].trim().isEmpty()) {
            String[] itens = partes[3].split("\\|");
            for (String itemTexto : itens) {
                String[] campos = itemTexto.split(",", -1);
                if (campos.length >= 3) {
                    try {
                        String codigoProduto = campos[0];
                        int quantidade = Integer.parseInt(campos[1]);
                        double preco = Double.parseDouble(campos[2]);
                        Produto produto = ProdutoCadastro.buscarPorCodigo(codigoProduto);
                        if (produto == null) {
                            produto = new Produto(codigoProduto, "Produto removido", 0, preco, "");
                        }
                        pedido.adicionarItem(new ItemPedido(produto, quantidade, preco));
                    } catch (NumberFormatException e) {
                        // ignora item inválido
                    }
                }
            }
        }
        return pedido;
    }

    private static void salvarObjetos(List<PedidoVenda> pedidos) {
        List<String> linhas = new ArrayList<>();
        for (PedidoVenda pedido : pedidos) {
            linhas.add(pedido.toLinhaArquivo());
        }
        ArquivoService.salvarLinhas(ARQUIVO, linhas);
    }

    private PedidoVenda montarPedido(String numeroPedido) {
        String codigoCliente = EntradaService.lerTextoObrigatorio("Código do cliente: ");
        Pessoa cliente = PessoaCadastro.buscarPorCodigo(codigoCliente);

        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
            return null;
        }

        if (!cliente.getTipoPessoa().podeComprar()) {
            System.out.println("A pessoa informada não é CLIENTE nem AMBOS.");
            return null;
        }

        List<Endereco> enderecos = EnderecoCadastro.listarPorPessoa(codigoCliente);
        if (enderecos.isEmpty()) {
            System.out.println("Esse cliente não possui endereço cadastrado.");
            return null;
        }

        System.out.println("\nEndereços do cliente:");
        for (Endereco endereco : enderecos) {
            System.out.println(endereco);
        }

        Endereco enderecoEscolhido = null;
        while (enderecoEscolhido == null) {
            String codigoEndereco = EntradaService.lerTextoObrigatorio("Código do endereço de entrega: ");
            for (Endereco endereco : enderecos) {
                if (endereco.getCodigo().equals(codigoEndereco)) {
                    enderecoEscolhido = endereco;
                    break;
                }
            }
            if (enderecoEscolhido == null) {
                System.out.println("Endereço inválido para esse cliente.");
            }
        }

        PedidoVenda pedido = new PedidoVenda(numeroPedido, codigoCliente, enderecoEscolhido.getCodigo());

        do {
            Produto produto = null;
            while (produto == null) {
                String codigoProduto = EntradaService.lerTextoObrigatorio("Código do produto: ");
                produto = ProdutoCadastro.buscarPorCodigo(codigoProduto);
                if (produto == null) {
                    System.out.println("Produto não encontrado.");
                }
            }

            int quantidade = EntradaService.lerInteiroPositivo("Quantidade: ");
            pedido.adicionarItem(new ItemPedido(produto, quantidade, produto.getPrecoVenda()));
            System.out.println("Item adicionado. Subtotal: R$ " + String.format("%.2f", quantidade * produto.getPrecoVenda()));

            String continuar = EntradaService.lerTexto("Adicionar outro produto? (S/N): ");
            if (!continuar.equalsIgnoreCase("S")) {
                break;
            }
        } while (true);

        if (pedido.getItens().isEmpty()) {
            System.out.println("Pedido precisa ter pelo menos um item.");
            return null;
        }

        return pedido;
    }

    private void imprimirPedidoCompleto(PedidoVenda pedido) {
        System.out.println("--------------------------------------------------");
        System.out.println(pedido);
        Pessoa cliente = PessoaCadastro.buscarPorCodigo(pedido.getCodigoCliente());
        if (cliente != null) {
            System.out.println("Cliente: " + cliente.getNome());
        }
        Endereco endereco = EnderecoCadastro.buscarPorCodigo(pedido.getCodigoEnderecoEntrega());
        if (endereco != null) {
            System.out.println("Entrega: " + endereco.getLogradouro() + ", " + endereco.getNumero() + " - CEP " + endereco.getCep());
        }
        System.out.println("Itens:");
        for (ItemPedido item : pedido.getItens()) {
            System.out.println("  " + item);
        }
        System.out.println("Total: R$ " + String.format("%.2f", pedido.calcularTotal()));
    }
}
