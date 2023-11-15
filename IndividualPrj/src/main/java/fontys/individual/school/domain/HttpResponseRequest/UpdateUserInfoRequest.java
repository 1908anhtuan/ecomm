package fontys.individual.school.domain.HttpResponseRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInfoRequest {

    @Builder.Default private String firstName = null;
    @Builder.Default private String lastName = null;
    @Builder.Default private String phoneNumber = null;
    @Builder.Default private String email = null;
}
