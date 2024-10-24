package rest;

import exceptions.NeededObjectNotFound;
import exceptions.NotEnoughData;
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

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400 Bad Request
    @ExceptionHandler(NotEnoughData.class)
    public ErrorResponseDto handleNotEnoughDataException(NotEnoughData e) {
        log.error("Not enough data: {}", e.getMessage(), e);
        return new ErrorResponseDto("Not enough data: " + e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND) // 404 Not Found
    @ExceptionHandler(NeededObjectNotFound.class)
    public ErrorResponseDto handleNeededObjectNotFoundException(NeededObjectNotFound e) {
        log.error("Needed object not found: {}", e.getMessage(), e);
        return new ErrorResponseDto("Needed object not found: " + e.getMessage());
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
