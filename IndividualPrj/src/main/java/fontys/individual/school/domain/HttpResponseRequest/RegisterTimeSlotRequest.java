package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.PickUpFacility;
import fontys.individual.school.domain.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterTimeSlotRequest {
    private Long digitalAdvertId;
    private TimeSlot timeSlot;
    private PickUpFacility facility;

}
