package br.com.meli.soccer.match_manager.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreationConflictException extends RuntimeException {
  private String details;

  public CreationConflictException(String details) {
    super("Invalid Field");
    this.details = details;
  }
}
