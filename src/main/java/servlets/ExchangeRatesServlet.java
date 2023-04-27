package servlets;

import dao.currency.Currency;
import dao.currency.CurrencyDAOImpl;
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
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();

        ExchangeRateService service = new ExchangeRateService();
        String jsonString = service.getAllExchangeRates();
        if (service.success()) {
            writer.println(jsonString);
        } else {
            service.sendErrorMessage(response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        resp.setContentType("application/json; charset=utf-8");

//        ExchangeRateDAO rateDAO = new ExchangeRateDAOImpl();
//        try {
//            ExchangeRate exchangeRate = rateDAO.addExchangeRate(baseCurrencyCode, targetCurrencyCode,
//                    Double.parseDouble(rate));
//            PrintWriter writer = resp.getWriter();
//            writer.println(exchangeRate.getId() + " " + exchangeRate.getRate());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }
}
