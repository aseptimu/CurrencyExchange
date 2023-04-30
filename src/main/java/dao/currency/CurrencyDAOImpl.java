package dao.currency;

import dao.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDAOImpl extends DBConnection implements CurrencyDAO {

    @Override
    public List<Currency> getAllCurrencies() throws SQLException {
        List<Currency> currencies = new ArrayList<>();

        String sql = "SELECT * FROM currencies";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                currencies.add(createCurrency(resultSet));
            }
        }
        return currencies;
    }

    @Override
    public Optional<Currency> getCurrencyByCode(String code) throws SQLException {
        String sql = "SELECT * FROM currencies WHERE code=?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();
            Currency currency = null;
            if (resultSet.next()) {
                currency = createCurrency(resultSet);
            }
            resultSet.close();
            return Optional.ofNullable(currency);
        }
    }

    @Override
    public Optional<Currency> addCurrency(Currency currency) throws SQLException {
        String sql = "INSERT INTO currencies (code, full_name, sign)" +
                "VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());
            statement.executeUpdate();
            return getCurrencyByCode(currency.getCode());
        }
    }

    private Currency createCurrency(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String code = resultSet.getString(2);
        String full_name = resultSet.getString(3);
        String sign = resultSet.getString(4);
        return new Currency(id, code, full_name, sign);
    }
}
