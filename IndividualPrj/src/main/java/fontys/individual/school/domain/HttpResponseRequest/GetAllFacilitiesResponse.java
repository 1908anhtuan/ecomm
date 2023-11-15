package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.PickUpFacility;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetAllFacilitiesResponse {
    private List<PickUpFacility> pickUpFacilityList;
}
