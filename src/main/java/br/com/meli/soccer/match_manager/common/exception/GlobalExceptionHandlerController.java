package br.com.meli.soccer.match_manager.common.exception;

import br.com.meli.soccer.match_manager.common.dto.response.ErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandlerController.class);

    @ExceptionHandler(InvalidFieldsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO invalidAcronymExceptionHandler(InvalidFieldsException exception) {
        return new ErrorResponseDTO(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST, exception.getDetails());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        Map<String, String> fieldsErrors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(field -> fieldsErrors.put(field.getField(), field.getDefaultMessage()));
        log.info(fieldsErrors.toString());
        return new ErrorResponseDTO(
                ((HttpStatus) exception.getStatusCode()).name(),
                (HttpStatus) exception.getStatusCode(),
                Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage(),
                fieldsErrors
        );
    }

    @ExceptionHandler(CreationConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDTO creationConflictExceptionHandler(CreationConflictException exception) {
        return new ErrorResponseDTO(HttpStatus.CONFLICT.name(), HttpStatus.CONFLICT, exception.getDetails());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO notFoundExceptionHandler(NotFoundException exception) {
        return new ErrorResponseDTO(HttpStatus.NOT_FOUND.name(), HttpStatus.NOT_FOUND, exception.getMessage());
    }
}
