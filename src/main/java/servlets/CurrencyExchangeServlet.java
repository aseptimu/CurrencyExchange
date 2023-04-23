package servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/exchange")
public class CurrencyExchangeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String amount = request.getParameter("amount");

//        response.setContentType("application/json");
        try (PrintWriter pw = response.getWriter()) {
            pw.println("from: " + from + "\nto: " + to + "\namount: " + amount);
        }
    }
}
