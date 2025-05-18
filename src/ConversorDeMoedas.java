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
    private static final String chaveApi = "INSIRA_SUA_CHAVE_AQUI";

    public static void main(String[] args) {
        ConversorDeMoedas conversor = new ConversorDeMoedas();
        while (true) {
            Scanner scanner = new Scanner(System.in);
            int escolha;
            String moedaBase;
            String moedaAlvo;
            BigDecimal quantia;
            System.out.println("""
                    ͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟
                                Bem-vindo ao conversor de moedas.
                    ̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅
                    💱 Digite o número correspondente à opção desejada abaixo.
                   \s
                    ( 1 ) Dólar americano[USD] >>> Peso argentino[ARS]
                    ( 2 ) Dólar americano[USD] >>> Real[BRL]
                    ( 3 ) Peso argentino[ARS] >>> Dólar americano[USD]
                    ( 4 ) Peso argentino[ARS] >>> Real[BRL]
                    ( 5 ) Real[BRL] >>> Dólar americano[USD]
                    ( 6 ) Real[BRL] >>> Peso argentino[ARS]
                   \s
                    ( 7 ) Histórico
                    ( 8 ) Encerrar
                   \s
                   """);
            try {
                escolha = scanner.nextInt();
                scanner.nextLine();
                if (escolha == 8){
                    break;
                } else{
                    switch (escolha) {
                        case 1 -> {
                            moedaBase = "USD";
                            moedaAlvo = "ARS";
                        }
                        case 2 -> {
                            moedaBase = "USD";
                            moedaAlvo = "BRL";
                        }
                        case 3 -> {
                            moedaBase = "ARS";
                            moedaAlvo = "USD";
                        }
                        case 4 -> {
                            moedaBase = "ARS";
                            moedaAlvo = "BRL";
                        }
                        case 5 -> {
                            moedaBase = "BRL";
                            moedaAlvo = "USD";
                        }
                        case 6 -> {
                            moedaBase = "BRL";
                            moedaAlvo = "ARS";
                        }
                        case 7 -> {
                            conversor.mostraHistorico();
                            continue;
                        }
                        default -> {
                            System.out.println("\nEscolha inválida.\n");
                            continue;
                        }
                    }
                    System.out.printf("""
                           \s
                            ﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍
                                          [%s] >>> [%s]
                            💱 Digite a quantia que deseja converter:
                            ﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍
                           """, moedaBase, moedaAlvo);
                    quantia = scanner.nextBigDecimal();
                    Moedas moedas = ConversorDeMoedas.pegaResultado(moedaBase, moedaAlvo, quantia);

                    ArrayList<String> numerosFormatados = ConversorDeMoedas.formataNumeros(quantia, moedas.conversion_result());
                    conversor.adicionaNoHistorico(numerosFormatados.getFirst(), moedaBase, numerosFormatados.getLast(), moedaAlvo);
                    System.out.printf("""
                                 ﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍
                                     Conversão realizada com sucesso:
                                 \s
                                     %s  [%s] = [%s]  %s
                                 ﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍﹍
                                \s
                                 💱 Digite qualquer tecla para continuar.
                                \s
                                 ❌ Digite ( 8 ) para encerrar o programa.
                                \s
                                """, numerosFormatados.getFirst(), moedaBase, moedaAlvo, numerosFormatados.getLast());
                    escolha = scanner.nextInt();
                    if (escolha == 8) {
                        break;
                    }
                }
            } catch (IllegalArgumentException | InputMismatchException | IllegalStateException e){
                System.out.print("""
                         ͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟͟
                        \s
                           Valor inválido.
                        \s
                         ̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅̅
                        """);
            }
        }
        System.out.println("\n Finalizando.");
    }

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
            URI url = URI.create("https://v6.exchangerate-api.com/v6/" + chaveApi + "/pair/" + moedaBase + "/" + moedaAlvo + "/" + quantia);
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
