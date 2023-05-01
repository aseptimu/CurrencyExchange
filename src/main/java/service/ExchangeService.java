package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.currency.Currency;
import dao.exchange.ExchangeRate;
import dao.exchange.ExchangeRateDAO;
import dao.exchange.ExchangeRateDAOImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Optional;

public class ExchangeService {
    private final ObjectMapper mapper;

    public ExchangeService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Optional<ObjectNode> exchange(String from, String to, double amount) throws SQLException {
        ExchangeRateDAO dao = new ExchangeRateDAOImpl();
        Optional<ExchangeRate> exchange = dao.getExchangeRateByCodes(from, to);
        if (exchange.isPresent()) {
            double convertedAmount = exchange.get().getRate() * amount;
            ObjectNode node = mapper.valueToTree(exchange.get());
            node.remove("id");
            node.put("amount", amount);
            node.put("convertedAmount", convertedAmount);
            return Optional.of(node);
        }
        exchange = dao.getExchangeRateByCodes(to, from);
        if (exchange.isPresent()) {
            ExchangeRate exchangeRate = exchange.get();
            BigDecimal rate = BigDecimal.valueOf(1).divide(
                    BigDecimal.valueOf(exchangeRate.getRate()), 6, RoundingMode.HALF_UP);
            exchangeRate.setRate(rate.doubleValue());
            ObjectNode root = sendResponse(exchangeRate.getBaseCurrency(),
                    exchangeRate.getTargetCurrency(), exchange.get().getRate(), amount);
            return Optional.of(root);
        }
        Optional<ExchangeRate> exchangeUSDA = dao.getExchangeRateByCodes("USD", from);
        Optional<ExchangeRate> exchangeUSDB = dao.getExchangeRateByCodes("USD", to);
        if (exchangeUSDA.isPresent() && exchangeUSDB.isPresent()) {
            BigDecimal rate = BigDecimal.valueOf(1)
                    .divide(BigDecimal.valueOf(exchangeUSDA.get().getRate()), 6, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(exchangeUSDB.get().getRate()));
            ObjectNode root = sendResponse(exchangeUSDB.get().getTargetCurrency(),
                    exchangeUSDA.get().getTargetCurrency(), rate.doubleValue(), amount);
            return Optional.of(root);
        }
        return Optional.empty();
    }
    private ObjectNode sendResponse(Currency base, Currency target, double rate, double amount) {
        double convertedAmount = rate * amount;
        ObjectNode rootNode = mapper.createObjectNode();
        ObjectNode baseCurrencyNode = mapper.valueToTree(target);
        ObjectNode targetCurrencyNode = mapper.valueToTree(base);
        rootNode.set("baseCurrency", baseCurrencyNode);
        rootNode.set("targetCurrency", targetCurrencyNode);
        rootNode.put("amount", amount);
        rootNode.put("convertedAmount", convertedAmount);
        return rootNode;
    }
}
