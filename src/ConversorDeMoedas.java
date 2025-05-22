import com.google.gson.Gson;
import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.util.Currency;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;


public class ConversorDeMoedas {

    private final List<String> historicoDeConversao = new ArrayList<>();
    private static final String CHAVE_API = "INSIRA_SUA_CHAVE_AQUI";

    public void adicionaNoHistorico(String quantia, String moedaBase, String resultado, String moedaAlvo){
        historicoDeConversao.add(quantia + " [" + moedaBase + "] >>> " + resultado + " [" + moedaAlvo + "]");
    }

    public void mostraHistorico() {
        System.out.println(""" 
                 ͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟
                                  Histórico de transações
                """);
        if (historicoDeConversao.isEmpty()) {
            System.out.println(" 💱 Nenhuma conversão foi realizada ainda!");
        } else {
            for (String resultado : historicoDeConversao) {
                System.out.printf("""
                          💱 %s
                        """, resultado);
            }
        }
        System.out.println(" ̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅");
    }

    public static boolean validaCodigoDeMoeda(String code) {
        try {
            Currency.getInstance(code.toUpperCase(Locale.ROOT));
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static ArrayList<String> formataNumeros(BigDecimal primeiroValor, BigDecimal segundoValor) {
        DecimalFormat formato = new DecimalFormat();
        ArrayList<String> resultados = new ArrayList<>();
        resultados.add(formato.format(primeiroValor));
        resultados.add(formato.format(segundoValor));
        return resultados;
    }

    public static Moedas pegaResultado(String moedaBase, String moedaAlvo, BigDecimal quantia){
        if (validaCodigoDeMoeda(moedaBase) && validaCodigoDeMoeda(moedaAlvo)){
            URI url = URI.create("https://v6.exchangerate-api.com/v6/" + CHAVE_API + "/pair/" + moedaBase + "/" + moedaAlvo + "/" + quantia);
            HttpRequest request = HttpRequest.newBuilder().uri(url).build();
            try {
                HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                return new Gson().fromJson(response.body(), Moedas.class);
            } catch (IOException | InterruptedException| IllegalStateException e) {
                throw new RuntimeException("Código da moeda não é válido.");
            }
        } else {
            System.out.println("Código de moeda inválido.");
            throw new RuntimeException();
        }
    }
}
