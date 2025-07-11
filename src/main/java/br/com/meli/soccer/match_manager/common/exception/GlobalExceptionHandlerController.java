package br.com.meli.soccer.match_manager.common.exception;

import br.com.meli.soccer.match_manager.common.dto.response.ErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandlerController.class);

    @ExceptionHandler(InvalidFieldsException.class)
    public ResponseEntity<ErrorResponseDTO> invalidAcronymExceptionHandler(InvalidFieldsException exception) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.name(), HttpStatus.BAD_REQUEST, exception.getDetails());
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                ((HttpStatus) exception.getStatusCode()).name(),
                (HttpStatus) exception.getStatusCode(),
                Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage());
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CreationConflictException.class)
    public ResponseEntity<ErrorResponseDTO> creationConflictExceptionHandler(CreationConflictException exception) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.CONFLICT.name(), HttpStatus.CONFLICT, exception.getDetails());
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> notFoundExceptionHandler(NotFoundException exception) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(HttpStatus.NOT_FOUND.name(), HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(errorResponseDTO.getStatus()).body(errorResponseDTO);
    }
}
