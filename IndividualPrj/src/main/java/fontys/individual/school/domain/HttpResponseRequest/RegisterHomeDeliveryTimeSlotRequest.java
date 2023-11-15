package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.HomeDeliveryAddress;
import fontys.individual.school.domain.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterHomeDeliveryTimeSlotRequest {
    private Long digitalAdvertId;
    private TimeSlot timeSlot;
    private HomeDeliveryAddress address;
    private LocalDate selectedDate;
}
