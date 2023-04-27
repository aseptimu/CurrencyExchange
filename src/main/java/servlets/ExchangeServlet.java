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

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String from = request.getParameter("from");
//        String to = request.getParameter("to");
//        String amount = request.getParameter("amount");
//
//        //TODO: валидация from to amount в service
//        ExchangeRateDAO dao = new ExchangeRateDAOImpl();
//        try {
//            ExchangeRate exchange = dao.getExchangeRateByCodes(from, to);
//            double result = exchange.getRate() * Double.parseDouble(amount);//TODO: bad request parsDouble
//            PrintWriter writer = response.getWriter();
//            writer.println(result);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }


    }
}
