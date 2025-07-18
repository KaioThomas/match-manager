package br.com.meli.soccer.match_manager.stadium.dto.request;

import lombok.*;

@ToString
@Getter
@Setter
public class StadiumCreateRequest extends StadiumRequest {

    public StadiumCreateRequest(String name) {
            super(name);
    }
}
