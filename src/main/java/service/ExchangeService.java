package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.currency.Currency;
import dao.exchange.ExchangeRate;
import dao.exchange.ExchangeRateDAO;
import dao.exchange.ExchangeRateDAOImpl;

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
            ObjectNode node = mapper.valueToTree(exchange.get());
            node.remove("id");
            node.put("amount", amount);
            node.put("convertedAmount", exchange.get().getRate() * amount);
            return Optional.of(node);
        }
        exchange = dao.getExchangeRateByCodes(to, from);
        if (exchange.isPresent()) {
            ExchangeRate exchangeRate = exchange.get();
            exchangeRate.setRate(1 / exchangeRate.getRate());
            ObjectNode root = sendResponse(exchangeRate.getBaseCurrency(),
                    exchangeRate.getTargetCurrency(), exchange.get().getRate(), amount);
            return Optional.of(root);
        }
        Optional<ExchangeRate> exchangeUSDA = dao.getExchangeRateByCodes("USD", from);
        Optional<ExchangeRate> exchangeUSDB = dao.getExchangeRateByCodes("USD", to);
        if (exchangeUSDA.isPresent() && exchangeUSDB.isPresent()) {
            double rate1 = 1 / exchangeUSDA.get().getRate();
            double rate2 = rate1 * exchangeUSDB.get().getRate();
            ObjectNode root = sendResponse(exchangeUSDB.get().getTargetCurrency(),
                    exchangeUSDA.get().getTargetCurrency(), rate2, amount);
            return Optional.of(root);
        }
        return Optional.empty();
    }
    private ObjectNode sendResponse(Currency base, Currency target, double rate, double amount) {
        ObjectNode rootNode = mapper.createObjectNode();
        ObjectNode baseCurrencyNode = mapper.valueToTree(target);
        ObjectNode targetCurrencyNode = mapper.valueToTree(base);
        rootNode.set("baseCurrency", baseCurrencyNode);
        rootNode.set("targetCurrency", targetCurrencyNode);
        rootNode.put("amount", amount);
        rootNode.put("convertedAmount", rate * amount);
        return rootNode;
    }
}
