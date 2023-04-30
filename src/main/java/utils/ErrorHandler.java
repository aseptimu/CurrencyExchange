package utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorHandler {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void sendError(int code, String message, HttpServletResponse response) {
        try {
            response.setStatus(code);
            response.getWriter().println();
            response.getWriter().println(MAPPER.createObjectNode().put("message", message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
