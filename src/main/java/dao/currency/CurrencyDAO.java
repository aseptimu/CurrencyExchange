package dao.currency;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CurrencyDAO {
    List<Currency> getAllCurrencies() throws SQLException;
    Optional<Currency> getCurrencyByCode(String code) throws SQLException;
    Optional<Currency> addCurrency(Currency currency) throws SQLException;
}
