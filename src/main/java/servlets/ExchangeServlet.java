package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import service.ExchangeService;
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

            ExchangeService service = new ExchangeService(mapper);
            Optional<ObjectNode> exchange = service.exchange(from.get(), to.get(), amount.get());
            if (exchange.isPresent()) {
                mapper.writeValue(response.getWriter(), exchange);
            } else {
                ErrorHandler.sendError(HttpServletResponse.SC_NOT_FOUND, "Exchange rate not found", response);
            }
        } catch (SQLException e) {
            ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error", response);
        } catch (Exception e) {
            ErrorHandler.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Fatal error", response);
        }
    }
}
