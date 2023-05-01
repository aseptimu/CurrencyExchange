package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.currency.Currency;
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
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private ObjectMapper mapper;
    @Override
    public void init() throws ServletException {
        super.init();
        mapper = new ObjectMapper();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            Optional<String> from = Validator.validateCurrencyCode(request.getParameter("from"));
            Optional<String> to = Validator.validateCurrencyCode(request.getParameter("to"));
            Optional<Double> amount = Validator.validateDouble(request.getParameter("amount"));

            if (from.isEmpty() || to.isEmpty() || amount.isEmpty()) {
                ErrorHandler.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "Required field is missing", response);
                return ;
            }

            ExchangeRateDAO dao = new ExchangeRateDAOImpl();
            Optional<ExchangeRate> exchange = dao.getExchangeRateByCodes(from.get(), to.get());
            if (exchange.isPresent()) {
                ObjectNode node = mapper.valueToTree(exchange.get());
                node.remove("id");
                node.put("amount", amount.get());
                node.put("convertedAmount", exchange.get().getRate() * amount.get());
                mapper.writeValue(response.getWriter(), node);
                return ;
            }
            exchange = dao.getExchangeRateByCodes(to.get(), from.get());
            if (exchange.isPresent()) {
                ExchangeRate exchangeRate = exchange.get();
                exchangeRate.setRate(1 / exchangeRate.getRate());
                ObjectNode root = sendResponse(exchangeRate.getBaseCurrency(),
                        exchangeRate.getTargetCurrency(), exchange.get().getRate(), amount.get());
                mapper.writeValue(response.getWriter(), root);
                return ;
            }
            Optional<ExchangeRate> exchangeUSDA = dao.getExchangeRateByCodes("USD", from.get());
            Optional<ExchangeRate> exchangeUSDB = dao.getExchangeRateByCodes("USD", to.get());
            if (exchangeUSDA.isPresent() && exchangeUSDB.isPresent()) {
                double rate1 = 1 / exchangeUSDA.get().getRate();
                double rate2 = rate1 * exchangeUSDB.get().getRate();
                ObjectNode root = sendResponse(exchangeUSDB.get().getTargetCurrency(),
                        exchangeUSDA.get().getTargetCurrency(), rate2, amount.get());
                mapper.writeValue(response.getWriter(), root);
                return ;
            }
            ErrorHandler.sendError(HttpServletResponse.SC_NOT_FOUND, "Exchange rate not found", response);
        } catch (SQLException e) {
            ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error", response);
        } catch (Exception e) {
            ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Fatal error", response);
        }
    }

    private ObjectNode sendResponse(Currency base, Currency target, double rate, double amount) {
        ObjectNode rootNode = mapper.createObjectNode();
        ObjectNode baseCurrencyNode = mapper.valueToTree(target);
        ObjectNode targetCurrencyNode = mapper.valueToTree(base);
        rootNode.set("baseCurrency", baseCurrencyNode);
        rootNode.set("targetCurrency", targetCurrencyNode);
        rootNode.put("amount", amount);
        rootNode.put("convertedAmount", rate * amount);
        return rootNode;
    }
}
