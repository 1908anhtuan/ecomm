package fontys.individual.school;

import fontys.individual.school.business.exception.InvalidAccessTokenException;
import fontys.individual.school.business.impl.AccessTokenEncoderDecoderImpl;
import fontys.individual.school.domain.AccessToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Key;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccessTokenEncoderDecoderImplTest {
    @Mock
    private Jwt jwt;

    private AccessTokenEncoderDecoderImpl encoderDecoder;

    private final String secretKey = "E91E158E4C6656F68B1B5D1C316766DE98D2AD6EF3BFB44F78E9CFCDF5";
    private final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        encoderDecoder = new AccessTokenEncoderDecoderImpl(secretKey);
    }

    @Test
    void encode_ValidAccessToken_ReturnsEncodedToken() {
        // Arrange
        AccessToken accessToken = AccessToken.builder()
                .subject("user123")
                .roles(Arrays.asList("role1", "role2"))
                .accountId(123L)
                .build();

        // Act
        String encodedToken = encoderDecoder.encode(accessToken);

        // Assert
        assertNotNull(encodedToken);
        assertTrue(encodedToken.length() > 0);
    }

    @Test
    void decode_InvalidEncodedToken_ThrowsInvalidAccessTokenException() {
        // Arrange
        String encodedToken = "yourInvalidEncodedToken";
        JwtException jwtException = mock(JwtException.class);
        when(jwtException.getMessage()).thenReturn("Invalid token");

        when(jwt.getBody()).thenThrow(jwtException);

        assertThrows(InvalidAccessTokenException.class, () -> encoderDecoder.decode(encodedToken));
    }
}