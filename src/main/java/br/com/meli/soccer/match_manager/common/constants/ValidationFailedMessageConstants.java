package br.com.meli.soccer.match_manager.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationFailedMessageConstants {

    public static final String NEGATIVE_NUMBER = "The number must be positive";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class CLUB {
        public static final String NOT_FOUND = "The club was not found";
        public static final String DUPLICATED = "A club with this name and from this state already exists";
        public static final String INVALID = "The club is invalid";
        public static final String INACTIVE = "The club is not active";
        public static final String CREATION_DATE_AFTER_MATCH = "The creation date cannot be after a previous match";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class MATCH {
        public static final String NOT_FOUND = "The match was not found";
        public static final String BEFORE_CLUB_CREATION_DATE =  "The match date cannot be before the clubs creation date";
        public static final String EQUAL_CLUBS = "The clubs at the match cannot be the same";
        public static final String INTERVAL_LESS_THAN_48_HOURS =  "The interval from last match must be greater than 48 hours";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class STADIUM {
        public static final String NOT_FOUND = "The stadium was not found";
        public static final String ALREADY_HAS_MATCH_AT_SAME_DAY =  "The stadium already has a match at the same day";
        public static final String DUPLICATED = "A stadium with this name already exists";
    }
}
