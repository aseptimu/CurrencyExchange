package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.currency.Currency;
import dao.exchange.ExchangeRate;
import dao.exchange.ExchangeRateDAO;
import dao.exchange.ExchangeRateDAOImpl;
import utils.ErrorHandler;
import utils.Validator;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Optional;

public class ExchangeRateService extends Service {
    private final ExchangeRateDAO dao = new ExchangeRateDAOImpl();

    public String getAllExchangeRates() {
        String response;
        try {
            response = mapper.writeValueAsString(dao.getAllExchangeRates());
        } catch (SQLException e) {
            errorHandler.setError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            response = e.getMessage();
        } catch (Exception e) {
            response = "Fatal error";
            errorHandler.setError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
        }
        return response;
    }

    public String getExchangeRate(String code) {
        Optional<String> validCurrency = Validator.validateExchangeRate(code);
        if (validCurrency.isEmpty()) {
            String error = "The pair's currency codes are missing from the address";
            errorHandler.setError(HttpServletResponse.SC_BAD_REQUEST, error);
            return error;
        }
        String pair = validCurrency.get();

        String response;
        try {
            Optional<ExchangeRate> exchangeRate = dao.getExchangeRateByCodes(pair.substring(0, 3), pair.substring(3));
            if (exchangeRate.isEmpty()) {
                response = "Exchange rate not found";
                errorHandler.setError(HttpServletResponse.SC_NOT_FOUND, response);
            } else {
                response = mapper.writeValueAsString(exchangeRate.get());
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

    public String addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, String rate) {
        String response;
        Optional<String> validBaseCode = Validator.validateCurrencyCode(baseCurrencyCode);
        Optional<String> validTargetCode = Validator.validateCurrencyCode(targetCurrencyCode);
        Optional<Double> validRate = Validator.validateRate(rate);
        if (validBaseCode.isEmpty() || validTargetCode.isEmpty() || validRate.isEmpty()) {
            response = "Required form field is missing";
            errorHandler.setError(HttpServletResponse.SC_BAD_REQUEST, response);
            return response;
        }

        try {
            Optional<ExchangeRate> exchangeRate = dao.addExchangeRate(validBaseCode.get(),
                                                    validTargetCode.get(),
                                                    validRate.get());
            if (exchangeRate.isEmpty()) {
                response = "Failed to get currency";
                errorHandler.setError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
            } else {
                response = mapper.writeValueAsString(exchangeRate.get());
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                response = "Currency pair with this code already exists";
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
