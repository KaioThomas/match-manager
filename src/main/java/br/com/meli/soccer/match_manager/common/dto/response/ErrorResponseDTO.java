package br.com.meli.soccer.match_manager.common.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponseDTO {

    private String message;
    private LocalDateTime timestamp;
    private int status;
    private String details;

    public ErrorResponseDTO(String message, HttpStatus status, String details) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.details = details;
    }
}
