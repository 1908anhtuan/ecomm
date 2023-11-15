package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.enumClasses.AdStatus;
import fontys.individual.school.domain.enumClasses.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetAdvertStatusRequest {
    private Long id;
    @Builder.Default private VerificationStatus verificationStatus = null;
    @Builder.Default private AdStatus advertStatus = null;
}
