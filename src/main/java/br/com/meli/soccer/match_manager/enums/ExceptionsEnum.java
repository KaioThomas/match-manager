package br.com.meli.soccer.match_manager.enums;

import lombok.Getter;

public enum ExceptionsEnum {

    CREATION_DATE_IN_FUTURE("The creation date cannot be in the future"),
    NON_EXISTENT_ACRONYM_STATE("The acronym state does not exist."),
    CLUB_EXISTS("Club already exists");

    @Getter
    private String value;

     ExceptionsEnum(String value) {
        this.value = value;
    }
}
