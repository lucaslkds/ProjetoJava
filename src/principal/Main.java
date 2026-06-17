package principal;

import cadastro.EnderecoCadastro;
import cadastro.PedidoCadastro;
import cadastro.PessoaCadastro;
import cadastro.ProdutoCadastro;
import sistema.ArquivoService;
import sistema.EntradaService;
import sistema.ConsoleService;
import sistema.MenuService;
import sistema.OpcaoMenu;

public class Main {
    public static void main(String[] args) {
        configurarUtf8NoConsole();
        prepararArquivos();

        PessoaCadastro pessoaCadastro = new PessoaCadastro();
        EnderecoCadastro enderecoCadastro = new EnderecoCadastro();
        ProdutoCadastro produtoCadastro = ProdutoCadastro.getInstancia();
        PedidoCadastro pedidoCadastro = new PedidoCadastro();

        int opcao;
        do {
            OpcaoMenu escolhida = MenuService.montarMenu("menus/menu-principal.txt");
            if (escolhida == null) {
                return;
            }
            opcao = escolhida.getOpcao();

            switch (opcao) {
                case 1:
                    pessoaCadastro.executarMenu();
                    break;
                case 2:
                    enderecoCadastro.executarMenu();
                    break;
                case 3:
                    produtoCadastro.executarMenu();
                    break;
                case 4:
                    pedidoCadastro.executarMenu();
                    break;
                case 0:
                    System.out.println("Sistema encerrado.");
                    break;
                default:
                    System.out.println("Opção inválida.");
                    EntradaService.aguardarEnter();
            }
        } while (opcao != 0);
    }

    private static void configurarUtf8NoConsole() {
        ConsoleService.configurarUtf8();
    }

    private static void prepararArquivos() {
        ArquivoService.garantirArquivo("dados/pessoas.txt");
        ArquivoService.garantirArquivo("dados/enderecos.txt");
        ArquivoService.garantirArquivo("dados/produtos.txt");
        ArquivoService.garantirArquivo("dados/pedidos.txt");
        ArquivoService.garantirArquivo("dados/log.txt");
    }
}
