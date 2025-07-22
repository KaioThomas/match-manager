package br.com.meli.soccer.match_manager.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SchemaConstants {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class CLUB {
        public static final String ID_DESC = "id do clube";
        public static final String ID_EXAMPLE = "e4446984-735d-4d18-a207-dad2632c2645";
        public static final String NAME_DESC = "nome do clube";
        public static final String NAME_EXAMPLE = "flamengo";
        public static final String GOALS_DESC = "gols feitos";
        public static final String GOALS_EXAMPLE = "5";
        public static final String ACRONYM_STATE_DESC = "sigla do estado";
        public static final String ACRONYM_STATE_EXAMPLE = "SP";
        public static final String DATE_DESC = "data de criação do clube";
        public static final String DATE_EXAMPLE = "2025-01-05";
        public static final String ACTIVE_DESC = "indica o clube esta ativo ou não";
        public static final String ACTIVE_EXAMPLE = "true";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class MATCH {
        public static final String ID_DESC = "id da partida";
        public static final String ID_EXAMPLE = "08d9cf76-214b-4e40-8811-3b85b4fdad03";
        public static final String DATE_DESC = "id do estádio";
        public static final String DATE_EXAMPLE = "2025-04-05";
        public static final String THRASHING_DESC = "filtro para partidas em que ocorreram goleadas";
        public static final String THRASHING_EXAMPLE = "true";
        public static final String CLUB_ACTING_DESC = "filtro para se o clube atuou como mandante ou visitante";
        public static final String CLUB_ACTING_EXAMPLE = "home";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class STADIUM {
        public static final String ID_DESC = "id do estádio";
        public static final String ID_EXAMPLE = "29abfd2e-7ac9-480c-9458-a55deaf74583";
        public static final String NAME_DESC = "nome do estádio";
        public static final String NAME_EXAMPLE = "maracanã";
    }

}
