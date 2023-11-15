package fontys.individual.school.domain.HttpResponseRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCountOfPickUpDeliveryFromAPeriodOfTimeRequest {
    private LocalDate startDate;
    private LocalDate endDate;
}
