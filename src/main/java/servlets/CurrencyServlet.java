package servlets;

import dao.currency.Currency;
import dao.currency.CurrencyDAOImpl;
import service.CurrencyService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json; charset=utf-8");

        CurrencyService service = new CurrencyService();
        String jsonString = service.getCurrency(request.getPathInfo());
        if (service.success()) {
            writer.println(jsonString);
        } else {
            service.sendErrorMessage(response);
        }
    }
}
