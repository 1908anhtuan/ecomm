package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.controller.Constraints.CloseTime;
import fontys.individual.school.controller.Constraints.OpenTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFacilityRequest {
    @NotNull
    @NotBlank
    private String facilityName;
    @NotNull
    @NotBlank
    private String city;
    @NotNull
    @NotBlank
    private String postCode;
    @OpenTime("05:00:00")
    @Builder.Default private LocalTime openTime = LocalTime.of(6,0);
    @CloseTime("23:00:00")
    @Builder.Default private LocalTime closeTime = LocalTime.of(22,0);
    @Min(5)
    @Builder.Default private Long maxLimitOfPickUpAtASameTime = 5L;
}
