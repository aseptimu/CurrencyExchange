package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDAOImpl implements CurrencyDAO {
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite::resource:currencies.db";

    public CurrencyDAOImpl() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Currency> getAllCurrencies() throws SQLException {
        List<Currency> currencies = new ArrayList<>();

        String sql = "SELECT * FROM currencies";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            String code = resultSet.getString(2);
            String full_name = resultSet.getString(3);
            String sign = resultSet.getString(4);
            Currency currency = new Currency(code, full_name, sign);
            currencies.add(currency);
        }
        return currencies;
    }

    @Override
    public Currency getCurrencyById(int id) throws SQLException {
        return null;
    }

    @Override
    public void addCurrency(Currency currency) throws SQLException {

    }
}
