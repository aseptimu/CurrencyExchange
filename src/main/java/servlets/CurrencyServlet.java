package servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currency = request.getPathInfo();
        PrintWriter pw = response.getWriter();
        if (currency == null) {
            pw.println("Error");
        } else {
            pw.println(currency);
        }
    }
}
