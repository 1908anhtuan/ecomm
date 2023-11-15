package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.TimeSlot;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetAvailableTimeSlotsResponse {
    private List<TimeSlot> timeSlotList;
}
