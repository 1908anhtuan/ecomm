package fontys.individual.school.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PickUpFacility {
    private Long id;
    private String facilityName;
    private String city;
    private String postCode;
    private LocalTime openTime;
    private LocalTime closeTime;

}
