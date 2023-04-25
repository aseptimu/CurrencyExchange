package servlets;

import dao.currency.Currency;
import dao.currency.CurrencyDAOImpl;
import service.CurrencyService;

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
        response.setContentType("application/json; charset=utf-8");
        PrintWriter pw = response.getWriter();

        CurrencyService service = new CurrencyService();
        pw.println(service.getAllCurrencies());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("name"); //TODO: protect all fields. name and code should be not null and prevent SQL injections
        String code = req.getParameter("code");

        String sign = req.getParameter("sign");
        Currency currency = new Currency(code, fullName, sign);
        CurrencyDAOImpl dao = new CurrencyDAOImpl();
        try {
            Currency currency1 = dao.addCurrency(currency);
            PrintWriter writer = resp.getWriter();
            writer.println(currency1.getId() + " " + currency.getCode() + " " + currency.getFullName() + " " + currency.getSign());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
