package fontys.individual.school.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeDeliveryAddress {
    private String postCode;
    private String city;
    private String street;
    private String houseNumber;
}
