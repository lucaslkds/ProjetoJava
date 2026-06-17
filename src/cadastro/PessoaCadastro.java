package cadastro;

import modelo.Pessoa;
import modelo.PessoaFactory;
import modelo.TipoPessoa;
import sistema.ArquivoService;
import sistema.EntradaService;
import sistema.LogService;
import sistema.MenuService;
import sistema.OpcaoMenu;
import sistema.TextoUtil;

import java.util.ArrayList;
import java.util.List;

public class PessoaCadastro {
    public static final String ARQUIVO = "dados/pessoas.txt";

    public void executarMenu() {
        int opcao;
        do {
            OpcaoMenu escolhida = MenuService.montarMenu("menus/menu-pessoas.txt");
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
        System.out.println("\n=== Incluir Pessoa ===");
        String codigo = EntradaService.lerTextoObrigatorio("Código: ");

        if (buscarPorCodigo(codigo) != null) {
            System.out.println("Já existe pessoa cadastrada com esse código.");
            return;
        }

        String nome = EntradaService.lerTextoObrigatorio("Nome: ");
        TipoPessoa tipo = escolherTipoPessoa();

        Pessoa pessoa = PessoaFactory.criarPorTipo(codigo, nome, tipo);
        ArquivoService.adicionarLinha(ARQUIVO, pessoa.toLinhaArquivo());
        LogService.registrar("INCLUSAO", "PESSOA", codigo);

        System.out.println("Pessoa cadastrada com sucesso.");
    }

    public void consultar() {
        System.out.println("\n=== Consultar Pessoa ===");
        String termo = EntradaService.lerTextoObrigatorio("Digite código ou parte do nome: ").toLowerCase();

        boolean encontrou = false;
        for (Pessoa pessoa : listarObjetos()) {
            if (pessoa.getCodigo().toLowerCase().contains(termo)
                    || pessoa.getNome().toLowerCase().contains(termo)) {
                System.out.println(pessoa);
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhuma pessoa encontrada.");
        }
    }

    public void alterar() {
        System.out.println("\n=== Alterar Pessoa ===");
        String codigo = EntradaService.lerTextoObrigatorio("Código da pessoa: ");
        List<Pessoa> pessoas = listarObjetos();

        for (int i = 0; i < pessoas.size(); i++) {
            Pessoa atual = pessoas.get(i);
            if (atual.getCodigo().equals(codigo)) {
                System.out.println("Registro atual: " + atual);
                String nome = EntradaService.lerTexto("Novo nome (ENTER mantém): ");
                if (!TextoUtil.vazio(nome)) {
                    atual.setNome(nome);
                }

                String alterarTipo = EntradaService.lerTexto("Alterar tipo de pessoa? (S/N): ");
                if (alterarTipo.equalsIgnoreCase("S")) {
                    atual.setTipoPessoa(escolherTipoPessoa());
                }

                pessoas.set(i, atual);
                salvarObjetos(pessoas);
                LogService.registrar("ALTERACAO", "PESSOA", codigo);
                System.out.println("Pessoa alterada com sucesso.");
                return;
            }
        }

        System.out.println("Pessoa não encontrada.");
    }

    public void excluir() {
        System.out.println("\n=== Excluir Pessoa ===");
        String codigo = EntradaService.lerTextoObrigatorio("Código da pessoa: ");

        if (pessoaEmUso(codigo)) {
            System.out.println("Não é possível excluir. Essa pessoa está vinculada a endereço, produto ou pedido.");
            return;
        }

        List<Pessoa> pessoas = listarObjetos();
        boolean removeu = pessoas.removeIf(p -> p.getCodigo().equals(codigo));

        if (removeu) {
            salvarObjetos(pessoas);
            LogService.registrar("EXCLUSAO", "PESSOA", codigo);
            System.out.println("Pessoa excluída com sucesso.");
        } else {
            System.out.println("Pessoa não encontrada.");
        }
    }

    public void listar() {
        System.out.println("\n=== Lista de Pessoas ===");
        List<Pessoa> pessoas = listarObjetos();
        if (pessoas.isEmpty()) {
            System.out.println("Nenhuma pessoa cadastrada.");
            return;
        }

        for (Pessoa pessoa : pessoas) {
            System.out.println(pessoa);
        }
    }

    public static Pessoa buscarPorCodigo(String codigo) {
        for (Pessoa pessoa : listarObjetos()) {
            if (pessoa.getCodigo().equals(codigo)) {
                return pessoa;
            }
        }
        return null;
    }

    public static List<Pessoa> listarObjetos() {
        List<Pessoa> pessoas = new ArrayList<>();
        for (String linha : ArquivoService.lerLinhas(ARQUIVO)) {
            Pessoa pessoa = Pessoa.fromLinhaArquivo(linha);
            if (pessoa != null) {
                pessoas.add(pessoa);
            }
        }
        return pessoas;
    }

    private static void salvarObjetos(List<Pessoa> pessoas) {
        List<String> linhas = new ArrayList<>();
        for (Pessoa pessoa : pessoas) {
            linhas.add(pessoa.toLinhaArquivo());
        }
        ArquivoService.salvarLinhas(ARQUIVO, linhas);
    }

    public static TipoPessoa escolherTipoPessoa() {
        while (true) {
            System.out.println("1 - Cliente");
            System.out.println("2 - Fornecedor");
            System.out.println("3 - Ambos");
            int opcao = EntradaService.lerInteiro("Tipo de pessoa: ");
            switch (opcao) {
                case 1:
                    return TipoPessoa.CLIENTE;
                case 2:
                    return TipoPessoa.FORNECEDOR;
                case 3:
                    return TipoPessoa.AMBOS;
                default:
                    System.out.println("Tipo inválido.");
            }
        }
    }

    private boolean pessoaEmUso(String codigoPessoa) {
        for (String linha : ArquivoService.lerLinhas(EnderecoCadastro.ARQUIVO)) {
            String[] partes = linha.split(";", -1);
            if (partes.length > 1 && partes[1].equals(codigoPessoa)) {
                return true;
            }
        }
        for (String linha : ArquivoService.lerLinhas(ProdutoCadastro.ARQUIVO)) {
            String[] partes = linha.split(";", -1);
            if (partes.length > 4 && partes[4].equals(codigoPessoa)) {
                return true;
            }
        }
        for (String linha : ArquivoService.lerLinhas(PedidoCadastro.ARQUIVO)) {
            String[] partes = linha.split(";", -1);
            if (partes.length > 1 && partes[1].equals(codigoPessoa)) {
                return true;
            }
        }
        return false;
    }
}
