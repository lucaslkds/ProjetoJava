package cadastro;

import modelo.Endereco;
import modelo.Pessoa;
import modelo.TipoEndereco;
import sistema.ArquivoService;
import sistema.EntradaService;
import sistema.LogService;
import sistema.MenuService;
import sistema.OpcaoMenu;
import sistema.TextoUtil;

import java.util.ArrayList;
import java.util.List;

public class EnderecoCadastro {
    public static final String ARQUIVO = "dados/enderecos.txt";

    public void executarMenu() {
        int opcao;
        do {
            OpcaoMenu escolhida = MenuService.montarMenu("menus/menu-enderecos.txt");
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
        System.out.println("\n=== Incluir Endereço ===");
        String codigo = EntradaService.lerTextoObrigatorio("Código do endereço: ");
        if (buscarPorCodigo(codigo) != null) {
            System.out.println("Já existe endereço com esse código.");
            return;
        }

        String codigoPessoa = EntradaService.lerTextoObrigatorio("Código da pessoa vinculada: ");
        Pessoa pessoa = PessoaCadastro.buscarPorCodigo(codigoPessoa);
        if (pessoa == null) {
            System.out.println("Pessoa não encontrada. Cadastre a pessoa antes do endereço.");
            return;
        }

        String cep = EntradaService.lerTextoObrigatorio("CEP: ");
        String logradouro = EntradaService.lerTextoObrigatorio("Logradouro: ");
        String numero = EntradaService.lerTextoObrigatorio("Número: ");
        String complemento = EntradaService.lerTexto("Complemento: ");
        TipoEndereco tipo = escolherTipoEndereco();

        Endereco endereco = Endereco.builder()
                .codigo(codigo)
                .codigoPessoa(codigoPessoa)
                .cep(cep)
                .logradouro(logradouro)
                .numero(numero)
                .complemento(complemento)
                .tipoEndereco(tipo)
                .build();
        ArquivoService.adicionarLinha(ARQUIVO, endereco.toLinhaArquivo());
        LogService.registrar("INCLUSAO", "ENDERECO", codigo);
        System.out.println("Endereço cadastrado com sucesso.");
    }

    public void consultar() {
        System.out.println("\n=== Consultar Endereço ===");
        String termo = EntradaService.lerTextoObrigatorio("Digite código, código da pessoa, CEP ou logradouro: ").toLowerCase();
        boolean encontrou = false;

        for (Endereco endereco : listarObjetos()) {
            if (endereco.getCodigo().toLowerCase().contains(termo)
                    || endereco.getCodigoPessoa().toLowerCase().contains(termo)
                    || endereco.getCep().toLowerCase().contains(termo)
                    || endereco.getLogradouro().toLowerCase().contains(termo)) {
                System.out.println(endereco);
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println("Nenhum endereço encontrado.");
        }
    }

    public void alterar() {
        System.out.println("\n=== Alterar Endereço ===");
        String codigo = EntradaService.lerTextoObrigatorio("Código do endereço: ");
        List<Endereco> enderecos = listarObjetos();

        for (int i = 0; i < enderecos.size(); i++) {
            Endereco atual = enderecos.get(i);
            if (atual.getCodigo().equals(codigo)) {
                System.out.println("Registro atual: " + atual);

                String cep = EntradaService.lerTexto("Novo CEP (ENTER mantém): ");
                if (!TextoUtil.vazio(cep)) atual.setCep(cep);

                String logradouro = EntradaService.lerTexto("Novo logradouro (ENTER mantém): ");
                if (!TextoUtil.vazio(logradouro)) atual.setLogradouro(logradouro);

                String numero = EntradaService.lerTexto("Novo número (ENTER mantém): ");
                if (!TextoUtil.vazio(numero)) atual.setNumero(numero);

                String complemento = EntradaService.lerTexto("Novo complemento (ENTER mantém): ");
                if (!TextoUtil.vazio(complemento)) atual.setComplemento(complemento);

                String alterarTipo = EntradaService.lerTexto("Alterar tipo de endereço? (S/N): ");
                if (alterarTipo.equalsIgnoreCase("S")) {
                    atual.setTipoEndereco(escolherTipoEndereco());
                }

                enderecos.set(i, atual);
                salvarObjetos(enderecos);
                LogService.registrar("ALTERACAO", "ENDERECO", codigo);
                System.out.println("Endereço alterado com sucesso.");
                return;
            }
        }

        System.out.println("Endereço não encontrado.");
    }

    public void excluir() {
        System.out.println("\n=== Excluir Endereço ===");
        String codigo = EntradaService.lerTextoObrigatorio("Código do endereço: ");

        if (enderecoEmPedido(codigo)) {
            System.out.println("Não é possível excluir. Esse endereço está vinculado a pedido.");
            return;
        }

        List<Endereco> enderecos = listarObjetos();
        boolean removeu = enderecos.removeIf(e -> e.getCodigo().equals(codigo));

        if (removeu) {
            salvarObjetos(enderecos);
            LogService.registrar("EXCLUSAO", "ENDERECO", codigo);
            System.out.println("Endereço excluído com sucesso.");
        } else {
            System.out.println("Endereço não encontrado.");
        }
    }

    public void listar() {
        System.out.println("\n=== Lista de Endereços ===");
        List<Endereco> enderecos = listarObjetos();
        if (enderecos.isEmpty()) {
            System.out.println("Nenhum endereço cadastrado.");
            return;
        }
        for (Endereco endereco : enderecos) {
            System.out.println(endereco);
        }
    }

    public static List<Endereco> listarPorPessoa(String codigoPessoa) {
        List<Endereco> filtrados = new ArrayList<>();
        for (Endereco endereco : listarObjetos()) {
            if (endereco.getCodigoPessoa().equals(codigoPessoa)) {
                filtrados.add(endereco);
            }
        }
        return filtrados;
    }

    public static Endereco buscarPorCodigo(String codigo) {
        for (Endereco endereco : listarObjetos()) {
            if (endereco.getCodigo().equals(codigo)) {
                return endereco;
            }
        }
        return null;
    }

    public static List<Endereco> listarObjetos() {
        List<Endereco> enderecos = new ArrayList<>();
        for (String linha : ArquivoService.lerLinhas(ARQUIVO)) {
            Endereco endereco = Endereco.fromLinhaArquivo(linha);
            if (endereco != null) {
                enderecos.add(endereco);
            }
        }
        return enderecos;
    }

    private static void salvarObjetos(List<Endereco> enderecos) {
        List<String> linhas = new ArrayList<>();
        for (Endereco endereco : enderecos) {
            linhas.add(endereco.toLinhaArquivo());
        }
        ArquivoService.salvarLinhas(ARQUIVO, linhas);
    }

    public static TipoEndereco escolherTipoEndereco() {
        while (true) {
            System.out.println("1 - Residencial");
            System.out.println("2 - Comercial");
            System.out.println("3 - Entrega");
            System.out.println("4 - Correspondência");
            int opcao = EntradaService.lerInteiro("Tipo de endereço: ");
            switch (opcao) {
                case 1:
                    return TipoEndereco.RESIDENCIAL;
                case 2:
                    return TipoEndereco.COMERCIAL;
                case 3:
                    return TipoEndereco.ENTREGA;
                case 4:
                    return TipoEndereco.CORRESPONDENCIA;
                default:
                    System.out.println("Tipo inválido.");
            }
        }
    }

    private boolean enderecoEmPedido(String codigoEndereco) {
        for (String linha : ArquivoService.lerLinhas(PedidoCadastro.ARQUIVO)) {
            String[] partes = linha.split(";", -1);
            if (partes.length > 2 && partes[2].equals(codigoEndereco)) {
                return true;
            }
        }
        return false;
    }
}
