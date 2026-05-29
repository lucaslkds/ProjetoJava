package sistema;

public class TextoUtil {
    private TextoUtil() {
    }

    public static String limpar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace(";", ",").replace("|", "/").replace("\n", " ").replace("\r", " ").trim();
    }

    public static boolean vazio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
