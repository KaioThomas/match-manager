package br.com.meli.soccer.match_manager.enums;

import lombok.Getter;

public enum ExceptionsEnum {

    NON_EXISTENT_ACRONYM_STATE("This acronym state does not exist."),
    CLUB_EXISTS("A club with this name and from this state already exists");

    @Getter
    private final String value;

     ExceptionsEnum(String value) {
        this.value = value;
    }
}
