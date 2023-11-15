package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.DigitalAdvert;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateAdStatusResponse {
    private DigitalAdvert advert;
}
