package sistema;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogService {
    private static final String ARQUIVO_LOG = "dados/log.txt";
    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LogService() {
    }

    public static void registrar(String operacao, String entidade, String codigo) {
        String dataHora = LocalDateTime.now().format(FORMATADOR);
        String linha = dataHora + " | " + operacao + " | " + entidade + " | CODIGO: " + codigo;
        ArquivoService.adicionarLinha(ARQUIVO_LOG, linha);
    }
}
