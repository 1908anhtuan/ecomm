package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.enumClasses.AdStatus;
import fontys.individual.school.domain.enumClasses.VerificationStatus;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

    @Data
    @Builder
    public class UpdateAdStatusRequest {
        private Optional<VerificationStatus> verificationStatus;
        private Optional<AdStatus> adStatus;
    }
