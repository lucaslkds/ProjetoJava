package sistema;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class EntradaService {
    private static final Scanner SCANNER = new Scanner(System.in, StandardCharsets.UTF_8.name());

    private EntradaService() {
    }

    public static String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return SCANNER.nextLine().trim();
    }

    public static String lerTextoObrigatorio(String mensagem) {
        String valor;
        do {
            valor = lerTexto(mensagem);
            if (valor.isEmpty()) {
                System.out.println("Campo obrigatório. Tente novamente.");
            }
        } while (valor.isEmpty());
        return valor;
    }

    public static int lerInteiro(String mensagem) {
        while (true) {
            String entrada = lerTexto(mensagem);
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Digite um número inteiro.");
            }
        }
    }

    public static int lerInteiroPositivo(String mensagem) {
        int valor;
        do {
            valor = lerInteiro(mensagem);
            if (valor <= 0) {
                System.out.println("Digite um número maior que zero.");
            }
        } while (valor <= 0);
        return valor;
    }

    public static double lerDouble(String mensagem) {
        while (true) {
            String entrada = lerTexto(mensagem).replace(",", ".");
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Exemplo válido: 10.50");
            }
        }
    }

    public static double lerDoubleNaoNegativo(String mensagem) {
        double valor;
        do {
            valor = lerDouble(mensagem);
            if (valor < 0) {
                System.out.println("Digite um valor maior ou igual a zero.");
            }
        } while (valor < 0);
        return valor;
    }

    public static void aguardarEnter() {
        System.out.println("\nPressione ENTER para continuar...");
        SCANNER.nextLine();
    }
}
