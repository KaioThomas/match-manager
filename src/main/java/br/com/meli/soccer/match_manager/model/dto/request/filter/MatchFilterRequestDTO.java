package br.com.meli.soccer.match_manager.model.dto.request.filter;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MatchFilterRequestDTO {

    private String homeClubId;
    private String stadiumId;
    private String visitingClubId;
    private Integer pageNumber;
    private Integer size;
    private String orderBy;
    private String direction;

    public String getVisitingClubId() {
        return visitingClubId != null ? visitingClubId : "0";
    }

    public String getStadiumId() {
        return stadiumId != null ? stadiumId : "0";
    }

    public String getHomeClubId() {
        return homeClubId != null ? homeClubId : "0";
    }

    public Integer getPageNumber() {
        return pageNumber != null ? pageNumber : 0;
    }

    public Integer getSize() {
        return size != null ? size : 10;
    }

    public String getOrderBy() {
        return orderBy != null ? orderBy : "homeClub.name";
    }

    public String getDirection() {
        return direction != null ? direction : "ASC";
    }
}
