package servlets;

import dao.exchange.ExchangeRate;
import dao.exchange.ExchangeRateDAO;
import dao.exchange.ExchangeRateDAOImpl;
import service.ExchangeRateService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase("PATCH")){
            doPatch(request, response);
        } else {
            super.service(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();

        ExchangeRateService service = new ExchangeRateService();
        String jsonString = service.getExchangeRate(request.getPathInfo());
        if (service.success()) {
            writer.println(jsonString);
        } else {
            service.sendErrorMessage(response);
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String baseCurrency = request.getPathInfo().substring(1, 4);
        String targetCurrency = request.getPathInfo().substring(4, 7);
        String rate = request.getParameter("rate");
        System.out.println(rate);

        ExchangeRateDAO rateDAO = new ExchangeRateDAOImpl();
        ExchangeRate rate1 = null;
        try {
            rate1 = rateDAO.updateExchangeRate(baseCurrency,
                    targetCurrency, Double.parseDouble(rate));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        PrintWriter writer = response.getWriter();
        writer.println(rate1.getRate());

    }
}
