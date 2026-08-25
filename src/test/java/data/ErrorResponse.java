package data;

import java.util.List;

public class ErrorResponse {
    private List<String> errors;
    private int status;

    public List<String> getErrors() {
        return errors;
    }

    public int getStatus() {
        return status;
    }
}
