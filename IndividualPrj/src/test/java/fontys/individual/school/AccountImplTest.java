package fontys.individual.school;

import fontys.individual.school.business.Interface.AccessTokenDecoder;
import fontys.individual.school.business.Interface.AccessTokenEncoder;
import fontys.individual.school.business.exception.AccountAlreadyExistsException;
import fontys.individual.school.business.exception.ForbiddenException;
import fontys.individual.school.business.exception.InvalidCredentialsException;
import fontys.individual.school.business.exception.UserNotFoundException;
import fontys.individual.school.business.impl.AccountServiceImpl;
import fontys.individual.school.domain.AccessToken;
import fontys.individual.school.domain.HttpResponseRequest.*;
import fontys.individual.school.domain.enumClasses.AccountType;
import fontys.individual.school.persistence.AccountRepository;
import fontys.individual.school.persistence.RoleRepository;
import fontys.individual.school.persistence.entity.AccountEntity;
import fontys.individual.school.persistence.entity.RoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class AccountImplTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccessTokenEncoder accessTokenEncoder;

    @Mock
    private AccessTokenDecoder accessTokenDecoder;

    @Mock
    private RoleRepository roleRepository;

    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountServiceImpl(
                accountRepository,
                passwordEncoder,
                accessTokenEncoder,
                accessTokenDecoder,
                roleRepository
        );
    }

    @Test
    void createAccount_ValidRequest_SuccessfullyCreatesAccount() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest();
        request.setAccountName("testuser");
        request.setPassword("password");

        when(accountRepository.existsByAccountName(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(accountRepository.save(any())).thenReturn(new AccountEntity());

        // Act
        CreateAccountResponse response = accountService.createAccount(request);

        // Assert
        assertNotNull(response);
        verify(accountRepository).existsByAccountName("testuser");
        verify(passwordEncoder).encode("password");
        verify(accountRepository).save(any());
        verify(roleRepository).save(any());
    }

    @Test
    void createAccount_AccountAlreadyExists_ThrowsAccountAlreadyExistsException() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest();
        request.setAccountName("existinguser");
        request.setPassword("password");

        when(accountRepository.existsByAccountName(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(AccountAlreadyExistsException.class, () -> accountService.createAccount(request));

        verify(accountRepository).existsByAccountName("existinguser");
        verifyNoInteractions(passwordEncoder, roleRepository);
    }

    @Test
    void signIn_ValidCredentials_ReturnsAccessToken() {
        // Arrange
        SignInRequest request = new SignInRequest();
        request.setAccountName("testuser");
        request.setPassword("password");

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(RoleEntity.builder().role(AccountType.Admin).build());
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setPassword("encodedPassword");
        accountEntity.setUserRoles(roles);
        when(accountRepository.findByAccountName(anyString())).thenReturn(Optional.of(accountEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(accessTokenEncoder.encode(any())).thenReturn("accessToken");

        // Act
        SignInResponse response = accountService.signIn(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getAccessToken());
        verify(accountRepository).findByAccountName("testuser");
        verify(passwordEncoder).matches("password", "encodedPassword");
        verify(accessTokenEncoder).encode(any());
    }

    @Test
    void signIn_UserNotFound_ThrowsUserNotFoundException() {
        // Arrange
        SignInRequest request = new SignInRequest();
        request.setAccountName("unknownuser");
        request.setPassword("password");

        when(accountRepository.findByAccountName(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> accountService.signIn(request));

        verify(accountRepository).findByAccountName("unknownuser");
        verifyNoInteractions(passwordEncoder, accessTokenEncoder);
    }

    @Test
    void signIn_InvalidCredentials_ThrowsInvalidCredentialsException() {
        // Arrange
        SignInRequest request = new SignInRequest();
        request.setAccountName("testuser");
        request.setPassword("wrongpassword");

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setPassword("encodedPassword");
        when(accountRepository.findByAccountName(anyString())).thenReturn(Optional.of(accountEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> accountService.signIn(request));

        verify(accountRepository).findByAccountName("testuser");
        verify(passwordEncoder).matches("wrongpassword", "encodedPassword");
        verifyNoMoreInteractions(accessTokenEncoder);
    }
    @Test
    void getAccountByAccountName_ExistingAccount_ReturnsAccount() {
        // Arrange
        GetAccountByAccountNameRequest request = new GetAccountByAccountNameRequest();
        request.setAccountName("testuser");

        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(1L);
        accountEntity.setAccountName("testuser");
        accountEntity.setPassword("encodedPassword");
//        accountEntity.setBalance(10000.0);
        when(accountRepository.findByAccountName(anyString())).thenReturn(Optional.of(accountEntity));

        // Act
        GetAccountByAccountNameResponse response = accountService.getAccountByAccountName(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getAccount());
        assertEquals(1L, response.getAccount().getId());
        assertEquals("testuser", response.getAccount().getAccountName());
        verify(accountRepository).findByAccountName("testuser");
    }

    @Test
    void getAccountByAccountName_NonExistingAccount_ThrowsUserNotFoundException() {
        // Arrange
        GetAccountByAccountNameRequest request = new GetAccountByAccountNameRequest();
        request.setAccountName("unknownuser");

        when(accountRepository.findByAccountName(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> accountService.getAccountByAccountName(request));

        verify(accountRepository).findByAccountName("unknownuser");
    }

    @Test
    void updateUserInfo_ValidRequestWithMatchingToken_SuccessfullyUpdatesUserInfo() {
        // Arrange
        Long accountId = 1L;
        UpdateUserInfoRequest request = new UpdateUserInfoRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhoneNumber("1234567890");
        request.setEmail("john.doe@example.com");
        String token = "validAccessToken";

        AccessToken accessToken = AccessToken.builder()
                .subject("testuser")
                .roles(Collections.singletonList("RegularUser"))
                .accountId(accountId)
                .build();

        when(accessTokenDecoder.decode(eq(token))).thenReturn(accessToken);
        when(accountRepository.updateUserInfo(
                eq(accountId),
                eq(request.getFirstName()),
                eq(request.getLastName()),
                eq(request.getPhoneNumber()),
                eq(request.getEmail())
        )).thenReturn(1);

        // Act
        UpdateUserInfoResponse response = accountService.updateUserInfo(accountId, request, token);

        // Assert
        assertNotNull(response);
        assertEquals("Successfully updated!", response.getMessage());
        verify(accessTokenDecoder).decode(token);
        verify(accountRepository).updateUserInfo(
                accountId,
                request.getFirstName(),
                request.getLastName(),
                request.getPhoneNumber(),
                request.getEmail()
        );
    }

    @Test
    void updateUserInfo_ValidRequestWithNonMatchingToken_ThrowsForbiddenException() {
        // Arrange
        Long accountId = 1L;
        UpdateUserInfoRequest request = new UpdateUserInfoRequest();
        String token = "invalidAccessToken";

        AccessToken accessToken = AccessToken.builder()
                .subject("otheruser")
                .roles(Collections.singletonList("RegularUser"))
                .accountId(2L)
                .build();

        when(accessTokenDecoder.decode(eq(token))).thenReturn(accessToken);

        // Act & Assert
        assertThrows(ForbiddenException.class, () -> accountService.updateUserInfo(accountId, request, token));

        verify(accessTokenDecoder).decode(token);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void updateUserInfo_ValidRequestWithMatchingTokenButUpdateFails_ReturnsFailToUpdateMessage() {
        // Arrange
        Long accountId = 1L;
        UpdateUserInfoRequest request = new UpdateUserInfoRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhoneNumber("1234567890");
        request.setEmail("john.doe@example.com");
        String token = "validAccessToken";

        AccessToken accessToken = AccessToken.builder()
                .subject("testuser")
                .roles(Collections.singletonList("RegularUser"))
                .accountId(accountId)
                .build();

        when(accessTokenDecoder.decode(eq(token))).thenReturn(accessToken);
        when(accountRepository.updateUserInfo(
                eq(accountId),
                eq(request.getFirstName()),
                eq(request.getLastName()),
                eq(request.getPhoneNumber()),
                eq(request.getEmail())
        )).thenReturn(0);

        // Act
        UpdateUserInfoResponse response = accountService.updateUserInfo(accountId, request, token);

        // Assert
        assertNotNull(response);
        assertEquals("Fail to update account!", response.getMessage());
        verify(accessTokenDecoder).decode(token);
        verify(accountRepository).updateUserInfo(
                accountId,
                request.getFirstName(),
                request.getLastName(),
                request.getPhoneNumber(),
                request.getEmail()
        );
    }
}
