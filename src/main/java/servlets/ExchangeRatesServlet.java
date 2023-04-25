package servlets;

import dao.currency.Currency;
import dao.currency.CurrencyDAOImpl;
import dao.exchange.ExchangeRate;
import dao.exchange.ExchangeRateDAO;
import dao.exchange.ExchangeRateDAOImpl;

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
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter writer = response.getWriter();

        ExchangeRateDAOImpl exchangeRateDAO = new ExchangeRateDAOImpl();
        try {
            List<ExchangeRate> rates = exchangeRateDAO.getAllExchangeRates();
            for (ExchangeRate rate : rates) {
                writer.println(rate.getId() + "\n" +
                        "baseCurrency:" +
                        "\n\t" + rate.getBaseCurrency().getId() +
                        "\n\t" + rate.getBaseCurrency().getFullName() +
                        "\n\t" + rate.getBaseCurrency().getCode() +
                        "\n\t" + rate.getBaseCurrency().getSign() + "\n" +
                        "targetCurrency:" +
                        "\n\t" + rate.getTargetCurrency().getId() +
                        "\n\t" + rate.getTargetCurrency().getFullName() +
                        "\n\t" + rate.getTargetCurrency().getCode() +
                        "\n\t" + rate.getTargetCurrency().getSign() + "\n" +
                        rate.getRate()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");

        ExchangeRateDAO rateDAO = new ExchangeRateDAOImpl();
        try {
            ExchangeRate exchangeRate = rateDAO.addExchangeRate(baseCurrencyCode, targetCurrencyCode,
                    Double.parseDouble(rate));
            PrintWriter writer = resp.getWriter();
            writer.println(exchangeRate.getId() + " " + exchangeRate.getRate());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
