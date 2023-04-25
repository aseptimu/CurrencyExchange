package servlets;

import dao.currency.Currency;
import dao.currency.CurrencyDAOImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currency = request.getPathInfo().substring(1);
        PrintWriter pw = response.getWriter();
        response.setContentType("text/plain; charset=utf-8");

        CurrencyDAOImpl currencyDAO = new CurrencyDAOImpl();
        try {
            Currency currencyByCode = currencyDAO.getCurrencyByCode(currency);
            pw.println(currencyByCode.getCode() + " " +
                    currencyByCode.getFullName() + " " +
                    currencyByCode.getSign());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
