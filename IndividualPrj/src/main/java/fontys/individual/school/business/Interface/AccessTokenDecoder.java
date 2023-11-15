package fontys.individual.school.business.Interface;

import fontys.individual.school.domain.AccessToken;

public interface AccessTokenDecoder {
    AccessToken decode(String accessTokenEncoded);
}
