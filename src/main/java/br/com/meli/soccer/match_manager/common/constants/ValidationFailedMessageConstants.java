package br.com.meli.soccer.match_manager.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationFailedMessageConstants {

    public static final String CLUB_NOT_FOUND = "The club was not found";
    public static final String STADIUM_NOT_FOUND = "The stadium was not found";
    public static final String MATCH_NOT_FOUND = "The match was not found";
    public static final String CLUB_DUPLICATED = "A club with this name and from this state already exists";
    public static final String CREATION_DATE_AFTER_MATCH = "The creation date cannot be after a previous match";
    public static final String EQUAL_CLUBS_AT_MATCH = "The clubs at the match cannot be the same";
    public static final String NEGATIVE_NUMBER = "The number must be positive";
    public static final String INVALID_STATE_ACRONYM = "This acronym state does not exist.";
    public static final String INVALID_CLUB = "The club is invalid";
    public static final String INACTIVE_CLUB = "The club is not active";
    public static final String MATCH_BEFORE_CREATION_DATE =  "The match date cannot be before the clubs creation date";
    public static final String INTERVAL_LESS_THAN_48_HOURS =  "The interval from last match must be greater than 48 hours";
    public static final String STADIUM_ALREADY_HAS_MATCH =  "The stadium already has a match at the same day";

}
