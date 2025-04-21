package hopla.routesmart.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final String error;
    private final LocalDateTime timestamp;

    public ApiException(String error, String message, HttpStatus status) {
        super(message);
        this.error = error;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}
