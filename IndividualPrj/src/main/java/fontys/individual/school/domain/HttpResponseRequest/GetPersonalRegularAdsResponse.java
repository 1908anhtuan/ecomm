package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.RegularAdvert;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetPersonalRegularAdsResponse {
    private List<RegularAdvert> personalRegularAdsList;
}
