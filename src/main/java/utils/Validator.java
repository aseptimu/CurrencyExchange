package utils;

import java.io.PrintWriter;
import java.util.Optional;

public class Validator {
    public static Optional<String> validateCurrencyCode(String currency) {
        String validCurrency = currency;
        if (!currency.isEmpty() && currency.charAt(0) == '/') {
            validCurrency = currency.substring(1);
        }
        if (!validCurrency.matches("^[A-Z]{3}$")) {
            return Optional.empty();
        }
        return Optional.of(validCurrency);
    }

    public static Optional<String> validateExchangeRate(String pair) {
        String validPair = pair;
        if (!pair.isEmpty() && pair.charAt(0) == '/') {
            validPair = pair.substring(1);
        }
        if (!validPair.matches("^[A-Z]{6}$")) {
            return Optional.empty();
        }
        return Optional.of(validPair);
    }

    public static Optional<Double> validateRate(String rate) {
        return Optional.empty();
    }
}
