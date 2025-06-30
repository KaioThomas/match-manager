package br.com.meli.soccer.match_manager.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidFieldsException extends RuntimeException {

    private String details;

    public InvalidFieldsException(String details) {
        super("Invalid Field");
        this.details = details;
    }
}
