import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {
        Principal principal = new Principal();
        principal.exibirMenu();
    }

    public void exibirMenu(){
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
}
