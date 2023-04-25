package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.currency.Currency;
import dao.currency.CurrencyDAOImpl;

import java.sql.SQLException;
import java.util.List;


public class CurrencyService {

    private static ObjectMapper mapper = new ObjectMapper();

    public String getAllCurrencies() {
        CurrencyDAOImpl dao = new CurrencyDAOImpl();
        String s;
        try {
            mapper = new ObjectMapper();
            s = mapper.writeValueAsString(dao.getAllCurrencies());

        } catch (SQLException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return s;
    }
}
