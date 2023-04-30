package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.currency.Currency;
import dao.currency.CurrencyDAO;
import dao.currency.CurrencyDAOImpl;
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

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private ObjectMapper mapper;
    @Override
    public void init() throws ServletException {
        super.init();
        mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Optional<String> validCurrency = Validator.validateCurrencyCode(request.getPathInfo());
            if (validCurrency.isEmpty()) {
                ErrorHandler.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Currency code is not specified in the address", response);
                return ;
            }

            PrintWriter writer = response.getWriter();
            CurrencyDAO dao = new CurrencyDAOImpl();
            Optional<Currency> currency = dao.getCurrencyByCode(validCurrency.get());
            if (currency.isPresent()) {
                String message = mapper.writeValueAsString(currency.get());
                writer.println(message);
            } else {
                ErrorHandler.sendError(HttpServletResponse.SC_NOT_FOUND, "Currency not found", response);
            }
        } catch (SQLException e) {
            ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error", response);
        } catch (Exception e) {
            ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Fatal error", response);
        }
    }
}
