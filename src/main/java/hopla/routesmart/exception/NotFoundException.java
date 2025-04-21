package hopla.routesmart.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {
    public NotFoundException(String error, String message) {
        super(error, message, HttpStatus.NOT_FOUND);
    }
}
