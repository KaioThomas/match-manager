package br.com.meli.soccer.match_manager.common.converter;

import br.com.meli.soccer.match_manager.common.enums.ClubTypeEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClubTypeEnumConverter implements Converter<String, ClubTypeEnum> {

    @Override
    public ClubTypeEnum convert(String type) {
        return ClubTypeEnum.valueOf(type.toUpperCase());
    }
}
