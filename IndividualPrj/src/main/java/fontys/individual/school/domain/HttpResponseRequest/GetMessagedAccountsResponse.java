package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.Account;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetMessagedAccountsResponse {
    private List<Account> accounts;
}
