package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.PickUpFacility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAvailableTimeSlotsRequest {
    private PickUpFacility   selectedFacility;

    private LocalDate selectedDate;
}
