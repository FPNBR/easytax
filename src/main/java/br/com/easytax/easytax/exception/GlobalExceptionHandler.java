package br.com.easytax.easytax.exception;

import br.com.easytax.easytax.util.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponse<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ApiResponse<>(false, ex.getMessage(), null);
    }

    @ResponseStatus(code = HttpStatus.CONFLICT)
    @ExceptionHandler(UniqueConstraintViolationException.class)
    public ApiResponse<String> handleDataIntegrityViolationException(UniqueConstraintViolationException ex) {
        return new ApiResponse<>(false, ex.getMessage(), null);
    }

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ApiResponse<String> handleBadCredentialsException(BadCredentialsException ex) {
        return new ApiResponse<>(false, ex.getMessage(), null);
    }

    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<String> handleAccessDeniedException(AccessDeniedException ex) {
        return new ApiResponse<>(false, ex.getMessage(), null);
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidUserInformation.class)
    public ApiResponse<String> handleInvalidPersonInformationException(InvalidUserInformation ex) {
        return new ApiResponse<>(false, ex.getMessage(), null);
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidCpfException.class)
    public ApiResponse<String> handleInvalidCpfException(InvalidCpfException ex) {
        return new ApiResponse<>(false, ex.getMessage(), null);
    }
}
