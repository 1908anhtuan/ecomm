package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.RegularAdvert;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BuyRegularAdvertResponse {
    private RegularAdvert regularAdvert;
}
