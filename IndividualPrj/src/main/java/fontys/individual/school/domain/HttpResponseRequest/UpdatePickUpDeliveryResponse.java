package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.PickUpDelivery;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdatePickUpDeliveryResponse {
    private boolean isUpdated;
}
