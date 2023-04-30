package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.exchange.ExchangeRate;
import dao.exchange.ExchangeRateDAO;
import dao.exchange.ExchangeRateDAOImpl;
import utils.ErrorHandler;
import utils.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private ObjectMapper mapper;
    @Override
    public void init() throws ServletException {
        super.init();
        mapper = new ObjectMapper();
    }
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase("PATCH")){
            doPatch(request, response);
        } else {
            super.service(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Optional<String> validCurrency = Validator.validateExchangeRate(request.getPathInfo());
            if (validCurrency.isEmpty()) {
                ErrorHandler.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "The pair's currency codes are missing from the address", response);
                return ;
            }

            PrintWriter writer = response.getWriter();
            ExchangeRateDAO dao = new ExchangeRateDAOImpl();

            String pair = validCurrency.get();
            Optional<ExchangeRate> exchangeRate = dao.getExchangeRateByCodes(pair.substring(0, 3), pair.substring(3));
            if (exchangeRate.isPresent()) {
                String message = mapper.writeValueAsString(exchangeRate.get());
                writer.println(message);
            } else {
                ErrorHandler.sendError(HttpServletResponse.SC_NOT_FOUND, "Exchange rate not found", response);
            }
        } catch (SQLException e) {
            ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error", response);
        } catch (Exception e) {
            ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Fatal error", response);
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) {
        Optional<String> validBaseCode = Validator.validateCurrencyCode(request.getPathInfo().substring(1, 4));
        Optional<String> validTargetCode = Validator.validateCurrencyCode(request.getPathInfo().substring(4, 7));
        try {
            String requestBody = request.getReader().readLine();
            Optional<Double> validRate = Validator.validatePATCHRate(requestBody);
            if (validBaseCode.isEmpty() || validTargetCode.isEmpty() || validRate.isEmpty()) {
                ErrorHandler.sendError(HttpServletResponse.SC_BAD_REQUEST, "Required form field is missing", response);
                return ;
            }

            PrintWriter writer = response.getWriter();
            ExchangeRateDAO dao = new ExchangeRateDAOImpl();

            Optional<ExchangeRate> exchangeRate = dao.updateExchangeRate(validBaseCode.get(),
                    validTargetCode.get(),
                    validRate.get());
            if (exchangeRate.isPresent()) {
                String message = mapper.writeValueAsString(exchangeRate.get());
                writer.println(message);
            } else {
                ErrorHandler.sendError(HttpServletResponse.SC_NOT_FOUND,
                        "The currency pair is missing in the database", response);
            }
        } catch (SQLException e) {
            ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error", response);
        } catch (Exception e) {
            ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Fatal error", response);
        }
    }
}
