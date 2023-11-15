package fontys.individual.school.domain.HttpResponseRequest;

import javax.validation.constraints.NotNull;

public class GetUserInfoRequest {
    @NotNull
    private long userId;
}
