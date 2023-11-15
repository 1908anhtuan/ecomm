package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.BiddingAdvert;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class GetAllBiddingAdvertsByFilterResponse {
    private List<BiddingAdvert> filteredBiddingAdList;
}
