package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidRefreshTokenException extends AccessDeniedException {
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
