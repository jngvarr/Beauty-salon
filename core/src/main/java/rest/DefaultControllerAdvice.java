package rest;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@ResponseStatus(HttpStatus.BAD_REQUEST)
@RestControllerAdvice
public class DefaultControllerAdvice {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponseDto accessDeniedException(AccessDeniedException e) {
        log.error("access denied: {}", e.getMessage(), e);
        return new ErrorResponseDto("access denied");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponseDto unexpectedError(Exception e) {
        ErrorResponseDto errorResponse = new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "unexpected error, provide the UUID to support team");
        log.error("unexpected error, message {}, UUID {}", e.getMessage(), errorResponse.getUuid(), e);
        return errorResponse;
    }
}
