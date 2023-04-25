package dao.exchange;

import dao.currency.Currency;

import java.sql.SQLException;
import java.util.List;

public interface ExchangeRateDAO {
    List<ExchangeRate> getAllExchangeRates() throws SQLException;
    ExchangeRate getExchangeRateByCodes(String baseCode, String targetCode) throws SQLException;
    ExchangeRate addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, double rate) throws SQLException;
    ExchangeRate updateExchangeRate(String baseCurrencyCode, String targetCurrencyCode, double rate) throws SQLException;
}
