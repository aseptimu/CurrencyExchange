package servlets;

import dao.Currency;
import dao.CurrencyDAOImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain; charset=utf-8");
        PrintWriter pw = response.getWriter();

        CurrencyDAOImpl dao = new CurrencyDAOImpl();
        try {
            List<Currency> currencies = dao.getAllCurrencies();
            for (Currency currency : currencies) {
                pw.println(currency.getCode() + " " +
                        currency.getFullName() + " " + currency.getSign());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("name"); //TODO: protect all fields. name and code should be not null and prevent SQL injections
        String code = req.getParameter("code");
        System.out.println(code.length());
        String sign = req.getParameter("sign");
        Currency currency = new Currency(code, fullName, sign);
        CurrencyDAOImpl dao = new CurrencyDAOImpl();
        try {
            dao.addCurrency(currency);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
