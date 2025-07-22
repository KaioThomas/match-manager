package br.com.meli.soccer.match_manager.common.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ErrorResponseDTO {

    private String message;
    private LocalDateTime timestamp;
    private int status;
    private String details;
    private Map<String, String> fields;

    public ErrorResponseDTO(String message, HttpStatus status, String details, Map<String, String> fields) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.details = details;
        this.fields = fields;
    }

    public ErrorResponseDTO(String message, HttpStatus status, String details) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.details = details;
        this.fields =  Map.of();
    }
}
