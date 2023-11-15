package fontys.individual.school.business.Interface;

import fontys.individual.school.domain.AccessToken;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
