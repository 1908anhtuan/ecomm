package fontys.individual.school.domain.HttpResponseRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyNowBiddingAdvertRequest {
    private Long userId;

}
