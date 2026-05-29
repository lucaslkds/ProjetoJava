package sistema;

import java.util.ArrayList;
import java.util.List;

public class MenuService {
    private MenuService() {
    }

    public static OpcaoMenu montarMenu(String caminhoArquivo) {
        List<OpcaoMenu> opcoes = carregarDoArquivo(caminhoArquivo);

        if (opcoes.isEmpty()) {
            System.out.println("Nenhuma opção encontrada no menu: " + caminhoArquivo);
            return null;
        }

        while (true) {
            exibirMenu(opcoes);
            int codigo = EntradaService.lerInteiro("Digite o número da opção desejada: ");

            for (OpcaoMenu opcao : opcoes) {
                if (opcao.getOpcao() == codigo) {
                    return opcao;
                }
            }

            System.out.println("Opção inválida. Tente novamente.");
        }
    }

    private static List<OpcaoMenu> carregarDoArquivo(String caminhoArquivo) {
        List<OpcaoMenu> opcoes = new ArrayList<>();
        List<String> linhas = ArquivoService.lerLinhas(caminhoArquivo);

        for (String linha : linhas) {
            if (linha == null || linha.trim().isEmpty() || linha.trim().startsWith("#")) {
                continue;
            }

            String[] partes = linha.split(";", 2);
            if (partes.length == 2) {
                try {
                    int codigo = Integer.parseInt(partes[0].trim());
                    String descricao = partes[1].trim();
                    opcoes.add(new OpcaoMenu(codigo, descricao));
                } catch (NumberFormatException e) {
                    System.out.println("Linha de menu inválida: " + linha);
                }
            }
        }

        return opcoes;
    }

    private static void exibirMenu(List<OpcaoMenu> opcoes) {
        System.out.println();
        for (OpcaoMenu opcao : opcoes) {
            System.out.println(opcao);
        }
    }
}
