package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.HomeDelivery;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateHomeDeliveryResponse {
    private boolean isUpdated;
}
