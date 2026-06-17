package sistema;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class ConsoleService {

    private ConsoleService() {
    }

    public static void configurarUtf8() {
        tentarConfigurarConsoleWindowsParaUtf8();
        configurarSaidaJavaParaUtf8();
    }

    private static void configurarSaidaJavaParaUtf8() {
        try {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8.name()));
            System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err), true, StandardCharsets.UTF_8.name()));
        } catch (Exception e) {
            System.out.println("Nao foi possivel configurar a saida UTF-8 no Java.");
        }
    }

    private static void tentarConfigurarConsoleWindowsParaUtf8() {
        if (!isWindows()) {
            return;
        }

        try {
            Process processo = new ProcessBuilder("cmd", "/c", "chcp 65001 > nul")
                    .inheritIO()
                    .start();
            processo.waitFor();
        } catch (Exception e) {
            // Se nao conseguir alterar a pagina de codigo do Windows,
            // o sistema continua funcionando normalmente.
            // Em alguns terminais antigos, os acentos podem aparecer incorretos.
        }
    }

    private static boolean isWindows() {
        String sistemaOperacional = System.getProperty("os.name");
        return sistemaOperacional != null && sistemaOperacional.toLowerCase().contains("win");
    }
}
