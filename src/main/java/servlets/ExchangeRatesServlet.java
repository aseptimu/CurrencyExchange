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
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private ObjectMapper mapper;
    @Override
    public void init() throws ServletException {
        super.init();
        mapper = new ObjectMapper();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            PrintWriter writer = response.getWriter();
            ExchangeRateDAO dao = new ExchangeRateDAOImpl();

            String message = mapper.writeValueAsString(dao.getAllExchangeRates());
            writer.println(message);
        } catch (SQLException e) {
            ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error", response);
        } catch (Exception e) {
            ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Fatal error", response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Optional<String> validBaseCode = Validator.validateCurrencyCode(req.getParameter("baseCurrencyCode"));
            Optional<String> validTargetCode = Validator.validateCurrencyCode(req.getParameter("targetCurrencyCode"));
            Optional<Double> validRate = Validator.validateDouble(req.getParameter("rate"));
            if (validBaseCode.isEmpty() || validTargetCode.isEmpty() || validRate.isEmpty()) {
                ErrorHandler.sendError(HttpServletResponse.SC_BAD_REQUEST, "Required form field is missing", resp);
                return ;
            }

            PrintWriter writer = resp.getWriter();
            ExchangeRateDAO dao = new ExchangeRateDAOImpl();

            Optional<ExchangeRate> exchangeRate = dao.addExchangeRate(validBaseCode.get(),
                    validTargetCode.get(),
                    validRate.get());
            if (exchangeRate.isPresent()) {
                String message = mapper.writeValueAsString(exchangeRate.get());
                writer.println(message);
            } else {
                ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to get currency", resp);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                ErrorHandler.sendError(HttpServletResponse.SC_CONFLICT, "Currency pair with this code already exists", resp);
            } else {
                ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error", resp);
            }
        } catch (Exception e) {
            ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Fatal error", resp);
        }
    }
}
