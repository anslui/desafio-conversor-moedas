import java.math.BigDecimal;

public record Moedas(String base_code, String target_code, double conversion_rate, BigDecimal conversion_result) {
    }

