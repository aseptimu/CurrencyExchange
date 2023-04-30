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

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
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
            CurrencyDAO dao = new CurrencyDAOImpl();

            String message = mapper.writeValueAsString(dao.getAllCurrencies());
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
            String fullName = req.getParameter("name");
            Optional<String> validCode = Validator.validateCurrencyCode(req.getParameter("code"));
            String sign = req.getParameter("sign");
            if (validCode.isEmpty() || fullName == null || sign == null) {
                ErrorHandler.sendError(HttpServletResponse.SC_BAD_REQUEST, "Required form field is missing", resp);
                return ;
            }
            String code = validCode.get();

            CurrencyDAO dao = new CurrencyDAOImpl();
            Currency currency = new Currency(code, fullName, sign);
            Optional<Currency> newCurrency = dao.addCurrency(currency);
            if (newCurrency.isPresent()) {
                String message = mapper.writeValueAsString(newCurrency.get());
                System.out.println(sign);
                System.out.println(newCurrency.get().getSign());
                System.out.println("\uD83D\uDE00");
                resp.getWriter().println(message);
            } else {
                ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to get currency", resp);
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                ErrorHandler.sendError(HttpServletResponse.SC_CONFLICT, "Currency with this code already exists", resp);
            } else {
                ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error", resp);
            }
        } catch (Exception e) {
            ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Fatal error", resp);
        }
    }
}
