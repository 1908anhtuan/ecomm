package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.BiddingAdvert;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateBiddingAdvertResponse {
    private BiddingAdvert biddingAdvert;
}
