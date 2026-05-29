package sistema;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ArquivoService {
    private ArquivoService() {
    }

    public static void garantirArquivo(String caminho) {
        try {
            Path path = Paths.get(caminho);
            Path pasta = path.getParent();
            if (pasta != null) {
                Files.createDirectories(pasta);
            }
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            System.out.println("Erro ao preparar arquivo " + caminho + ": " + e.getMessage());
        }
    }

    public static List<String> lerLinhas(String caminho) {
        garantirArquivo(caminho);
        try {
            return Files.readAllLines(Paths.get(caminho), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo " + caminho + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void salvarLinhas(String caminho, List<String> linhas) {
        garantirArquivo(caminho);
        try {
            Files.write(Paths.get(caminho), linhas, StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo " + caminho + ": " + e.getMessage());
        }
    }

    public static void adicionarLinha(String caminho, String linha) {
        garantirArquivo(caminho);
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(caminho), StandardCharsets.UTF_8,
                StandardOpenOption.APPEND)) {
            writer.write(linha);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao adicionar linha no arquivo " + caminho + ": " + e.getMessage());
        }
    }
}
