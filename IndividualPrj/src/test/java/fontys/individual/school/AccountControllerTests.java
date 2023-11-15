package fontys.individual.school;

import fontys.individual.school.business.Interface.AccountUseCases;
import fontys.individual.school.controller.AccountController;
import fontys.individual.school.domain.HttpResponseRequest.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountControllerTests {
    @Mock
    private AccountUseCases accountUseCase;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createAccount_ShouldReturnCreatedStatus() {
        // Arrange
        CreateAccountRequest request = new CreateAccountRequest();
        CreateAccountResponse expectedResponse = CreateAccountResponse.builder().build();
        when(accountUseCase.createAccount(any(CreateAccountRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<CreateAccountResponse> response = accountController.createAccount(request);

        // Assert
        assert response.getStatusCode() == HttpStatus.CREATED;
        assert response.getBody() == expectedResponse;
        verify(accountUseCase, times(1)).createAccount(any(CreateAccountRequest.class));
    }

    @Test
    void signIn_ShouldReturnCreatedStatus() {
        // Arrange
        SignInRequest request = new SignInRequest();
        SignInResponse expectedResponse = SignInResponse.builder().build();

        when(accountUseCase.signIn(any(SignInRequest.class))).thenReturn(expectedResponse);

        // Act
        ResponseEntity<SignInResponse> response = accountController.signIn(request);

        // Assert
        assert response.getStatusCode() == HttpStatus.CREATED;
        assert response.getBody() == expectedResponse;
        verify(accountUseCase, times(1)).signIn(any(SignInRequest.class));
    }

}
