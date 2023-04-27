package dao.exchange;

import dao.currency.Currency;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateDAO {
    List<ExchangeRate> getAllExchangeRates() throws SQLException;
    Optional<ExchangeRate> getExchangeRateByCodes(String baseCode, String targetCode) throws SQLException;
    Optional<ExchangeRate> addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, double rate) throws SQLException;
    ExchangeRate updateExchangeRate(String baseCurrencyCode, String targetCurrencyCode, double rate) throws SQLException;
}
