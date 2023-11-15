package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.Account;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetAccountByAccountNameResponse {
    private Account account;
}
