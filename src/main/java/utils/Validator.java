package utils;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static Optional<Double> validateDouble(String rate) {
        try {
            Double num = Double.parseDouble(rate);
            return Optional.of(num);
        } catch (NumberFormatException | NullPointerException exception) {
            return Optional.empty();
        }
    }

    public static Optional<Double> validatePATCHRate(String requestBody) {
        if (requestBody == null) {
            return Optional.empty();
        }
        Pattern pattern = Pattern.compile("rate=([+-]?\\d+(\\.\\d+)?)(?=\\D|$)");
        Matcher matcher = pattern.matcher(requestBody);
        if (matcher.find()) {
            String validRate = matcher.group(1);
            double rate = Double.parseDouble(validRate);
            return Optional.of(rate);
        }
        return Optional.empty();
    }
}
