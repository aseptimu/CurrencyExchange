package servlets;

import dao.exchange.ExchangeRate;
import dao.exchange.ExchangeRateDAO;
import dao.exchange.ExchangeRateDAOImpl;

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
        String currency = request.getPathInfo().substring(1);
        PrintWriter pw = response.getWriter();
        response.setContentType("text/plain; charset=utf-8");
        if (!currency.matches("^[A-Z]{6}$")) {
            pw.println("Error!\n");//TODO: 400
        }

        ExchangeRateDAO dao = new ExchangeRateDAOImpl();
        try {
            ExchangeRate rate = dao.getExchangeRateByCodes(currency.substring(0, 3),
                    currency.substring(3));
            pw.println(rate.getId() + "\n" +
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
