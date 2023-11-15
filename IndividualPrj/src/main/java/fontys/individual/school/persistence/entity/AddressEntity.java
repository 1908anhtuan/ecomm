package fontys.individual.school.persistence.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddressEntity {
    private String postCode;
    private String city;
    private String street;
    private String houseNumber;
}
