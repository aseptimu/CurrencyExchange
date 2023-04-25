package dao.exchange;

import dao.currency.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDAOImpl implements ExchangeRateDAO {

    private Connection connection;//TODO: duplicated code for connection
    private static final String DB_URL = "jdbc:sqlite::resource:currencies.db";

    public ExchangeRateDAOImpl() {
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
    public List<ExchangeRate> getAllExchangeRates() throws SQLException {
        List<ExchangeRate> rates = new ArrayList<>();
        String sql = """
                SELECT exchange_rates.id, base.*, target.*, exchange_rates.rate
                FROM exchange_rates
                JOIN currencies base on exchange_rates.base_currency_id = base.id
                JOIN currencies target on exchange_rates.target_currency_id = target.id""";
        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);
        while (result.next()) {
            rates.add(getExchangeFromQuery(result));
        }
        return rates;
    }

    private ExchangeRate getExchangeFromQuery(ResultSet result) throws SQLException {
        int exchangeRateId = result.getInt(1);

        int currencyId = result.getInt(2);
        String currencyCode = result.getString(3);
        String currencyFullName = result.getString(4);
        String currencySign = result.getString(5);
        Currency base = new Currency(currencyId, currencyCode, currencyFullName, currencySign);

        currencyId = result.getInt(6);
        currencyCode = result.getString(7);
        currencyFullName = result.getString(8);
        currencySign = result.getString(9);
        Currency target = new Currency(currencyId, currencyCode, currencyFullName, currencySign);

        double rate = result.getDouble(10);

        return new ExchangeRate(exchangeRateId, base, target, rate);
    }

    @Override
    public ExchangeRate getExchangeRateByCodes(String baseCode, String targetCode) throws SQLException {
        String sql = """
                SELECT exchange_rates.id, base.*, target.*, exchange_rates.rate
                FROM exchange_rates
                JOIN currencies base on exchange_rates.base_currency_id = base.id
                JOIN currencies target on exchange_rates.target_currency_id = target.id
                WHERE base.code = ? AND target.code = ?""";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, baseCode);
        statement.setString(2, targetCode);
        ResultSet result = statement.executeQuery();
        return getExchangeFromQuery(result);
    }

    @Override
    public ExchangeRate addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, double rate) throws SQLException {
        String sql = """
                INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
                VALUES ((SELECT id FROM currencies WHERE code=?),
                        (SELECT id FROM currencies WHERE code=?),
                        ?)""";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, baseCurrencyCode);
        statement.setString(2, targetCurrencyCode);
        statement.setDouble(3, rate);
        statement.executeUpdate(); //TODO: падает после другого POST запроса
        return getExchangeRateByCodes(baseCurrencyCode, targetCurrencyCode);
    }

    @Override
    public ExchangeRate updateExchangeRate(String baseCurrencyCode, String targetCurrencyCode, double rate) throws SQLException {
        String sql = """
                UPDATE exchange_rates SET rate = ?
                WHERE (SELECT id FROM currencies WHERE code=?)=base_currency_id
                AND (SELECT id FROM currencies WHERE code=?)=target_currency_id
                        """;

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setDouble(1, rate);
        statement.setString(2, baseCurrencyCode);
        statement.setString(3, targetCurrencyCode);
        statement.executeUpdate();
        return getExchangeRateByCodes(baseCurrencyCode, targetCurrencyCode);
    }
}
