package fontys.individual.school.domain.HttpResponseRequest;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class SetAdvertStatusResponse {
    private Boolean isUpdated;

}
