package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorHandler {
    private boolean error;
    private int errorCode;
    private String errorMessage;
    private ObjectMapper mapper = new ObjectMapper();

    public String getError() throws JsonProcessingException {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("message", errorMessage);
        return mapper.writeValueAsString(objectNode);
    }

    public void setError(int errorCode, String message) {
        this.error = true;
        this.errorCode = errorCode;
        this.errorMessage = message;
    }

    public boolean isError() {
        return error;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
