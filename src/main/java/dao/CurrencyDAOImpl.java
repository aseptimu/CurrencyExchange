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
            currencies.add(createCurrency(resultSet));
        }
        return currencies;
    }

    @Override
    public Currency getCurrencyByCode(String code) throws SQLException {
        String sql = "SELECT * FROM currencies WHERE code=?";//TODO: sql injection protection

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, code);
        ResultSet resultSet = statement.executeQuery();
        return createCurrency(resultSet);
    }

    @Override
    public void addCurrency(Currency currency) throws SQLException {
        String sql = "INSERT INTO currencies (code, full_name, sign)" +
                "VALUES (?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, currency.getCode());
        statement.setString(2, currency.getFullName());
        statement.setString(3, currency.getSign());
        statement.executeUpdate();
        connection.commit();
    }

    private Currency createCurrency(ResultSet resultSet) throws SQLException {
        String code = resultSet.getString(2);
        String full_name = resultSet.getString(3);
        String sign = resultSet.getString(4);
        return new Currency(code, full_name, sign);
    }
}
