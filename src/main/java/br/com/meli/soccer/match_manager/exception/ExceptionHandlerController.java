package br.com.meli.soccer.match_manager.exception;

import br.com.meli.soccer.match_manager.dto.response.ErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(InvalidFieldsException.class)
    public ResponseEntity<ErrorResponseDTO> invalidAcronymExceptionHandler(InvalidFieldsException exception) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(exception.getMessage(), HttpStatus.BAD_REQUEST, exception.getDetails());
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(exception.getBody().getTitle(), (HttpStatus) exception.getStatusCode(), exception.getBody().getDetail());
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(CreationConflictException.class)
    public ResponseEntity<ErrorResponseDTO> creationConflictExceptionHandler(CreationConflictException exception) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(exception.getMessage(), HttpStatus.CONFLICT, exception.getDetails());
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }
}
