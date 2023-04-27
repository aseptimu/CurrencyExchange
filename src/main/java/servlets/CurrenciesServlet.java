package servlets;

import dao.currency.Currency;
import dao.currency.CurrencyDAOImpl;
import service.CurrencyService;
import utils.ErrorHandler;

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
        PrintWriter writer = response.getWriter();

        CurrencyService service = new CurrencyService();
        String jsonString = service.getAllCurrencies();
        if (service.success()) {
            writer.println(jsonString);
        } else {
            service.sendErrorMessage(response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        resp.setContentType("application/json; charset=utf-8");

        CurrencyService service = new CurrencyService();
        String jsonString = service.addCurrency(code, fullName, sign);
        if (service.success()) {
            resp.getWriter().println(jsonString);
        } else  {
            service.sendErrorMessage(resp);
        }
    }
}
