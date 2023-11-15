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
public class GetAllRegularAdvertsByFilterRequest {
    @Builder.Default private Long categoryId = null;
    @Builder.Default private VerificationStatus verificationStatus = null;
    @Builder.Default private AdStatus adStatus = null;
}
