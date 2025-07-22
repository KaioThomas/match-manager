package br.com.meli.soccer.match_manager.stadium.dto.request;

import br.com.meli.soccer.match_manager.common.constants.SchemaConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class StadiumRequest {
        @NotEmpty
        @Size(min = 3, max = 50)
        @Schema(description = SchemaConstants.STADIUM.NAME_DESC, example = SchemaConstants.STADIUM.NAME_EXAMPLE)
        private String name;
}
