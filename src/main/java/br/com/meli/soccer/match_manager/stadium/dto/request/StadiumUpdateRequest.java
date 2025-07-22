package br.com.meli.soccer.match_manager.stadium.dto.request;

import br.com.meli.soccer.match_manager.common.constants.SchemaConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class StadiumUpdateRequest extends StadiumRequest {
        @NotEmpty
        @Schema(description = SchemaConstants.STADIUM.ID_DESC, example = SchemaConstants.STADIUM.ID_EXAMPLE)
        String id;

        public StadiumUpdateRequest(String id, String name) {
                super(name);
                this.id = id;
        }
}
