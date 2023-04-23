package dao;

import java.sql.SQLException;
import java.util.List;

public interface CurrencyDAO {
    List<Currency> getAllCurrencies() throws SQLException;
    Currency getCurrencyByCode(String code) throws SQLException;
    void addCurrency(Currency currency) throws SQLException;

}
