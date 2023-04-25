package dao.currency;

import java.sql.SQLException;
import java.util.List;

public interface CurrencyDAO {
    List<Currency> getAllCurrencies() throws SQLException;
    Currency getCurrencyByCode(String code) throws SQLException;
    Currency addCurrency(Currency currency) throws SQLException;

}
