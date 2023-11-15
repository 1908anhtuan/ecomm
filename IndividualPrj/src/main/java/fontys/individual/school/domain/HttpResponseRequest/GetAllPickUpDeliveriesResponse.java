package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.PickUpDelivery;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetAllPickUpDeliveriesResponse {
    private List<PickUpDelivery> deliveries;
}
