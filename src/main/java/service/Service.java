package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import utils.ErrorHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class Service {
    protected final ObjectMapper mapper = new ObjectMapper();
    protected final ErrorHandler errorHandler = new ErrorHandler();

    public boolean success() {
        return !errorHandler.isError();
    }

    public void sendErrorMessage(HttpServletResponse response) {
        String message = "OK";
        try {
            if (errorHandler.isError()) {
                response.setStatus(errorHandler.getErrorCode());
                message = errorHandler.getError();
            }
            response.getWriter().println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
