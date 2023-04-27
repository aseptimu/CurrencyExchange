package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.currency.Currency;
import dao.currency.CurrencyDAO;
import dao.currency.CurrencyDAOImpl;
import utils.ErrorHandler;
import utils.Validator;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;


public class CurrencyService extends Service {
    private final CurrencyDAO dao = new CurrencyDAOImpl();

    public String getAllCurrencies() {
        String response;
        try {
            response = mapper.writeValueAsString(dao.getAllCurrencies());
        } catch (SQLException e) {
            errorHandler.setError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            response = e.getMessage();
        } catch (Exception e) {
            response = "Fatal error";
            errorHandler.setError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
        }
        return response;
    }

    public String getCurrency(String code) {
        Optional<String> validCurrency = Validator.validateCurrencyCode(code);
        if (validCurrency.isEmpty()) {
            String error = "Currency code is not specified in the address";
            errorHandler.setError(HttpServletResponse.SC_BAD_REQUEST, error);
            return error;
        }

        String response;
        try {
            Optional<Currency> currency = dao.getCurrencyByCode(validCurrency.get());
            if (currency.isEmpty()) {
                response = "Currency not found";
                errorHandler.setError(HttpServletResponse.SC_NOT_FOUND, response);
            } else {
                response = mapper.writeValueAsString(currency.get());
            }
        } catch (SQLException e) {
            errorHandler.setError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            response = e.getMessage();
        } catch (Exception e) {
            response = "Fatal error";
            errorHandler.setError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
        }
        return response;
    }

    public String addCurrency(String code, String fullName, String sign) {
        String response;
        Optional<String> validCode = Validator.validateCurrencyCode(code);
        if (validCode.isEmpty() || fullName == null || sign == null) {
            response = "Required form field is missing";
            errorHandler.setError(HttpServletResponse.SC_BAD_REQUEST, response);
            return response;
        }

        Currency currency = new Currency(code, fullName, sign);
        try {
            Optional<Currency> newCurrency = dao.addCurrency(currency);
            if (newCurrency.isEmpty()) {
                response = "Failed to get currency";
                errorHandler.setError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
            } else {
                response = mapper.writeValueAsString(newCurrency.get());
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                response = "Currency with this code already exists";
                errorHandler.setError(HttpServletResponse.SC_CONFLICT, response);
            } else {
                response = e.getMessage();
                errorHandler.setError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
            }
        } catch (Exception e) {
            response = "Fatal error";
            errorHandler.setError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
        }
        return response;
    }
}
