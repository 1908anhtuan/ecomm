package fontys.individual.school.controller;

import fontys.individual.school.business.Interface.AccountUseCases;

import fontys.individual.school.configuration.security.isauthenticated.IsAuthenticated;
import fontys.individual.school.domain.HttpResponseRequest.GetAllAccountsResponse;
import fontys.individual.school.domain.HttpResponseRequest.*;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
public class AccountController {
    private final AccountUseCases accountUseCase;

    @PostMapping()
    public ResponseEntity<CreateAccountResponse> createAccount(@RequestBody @Valid CreateAccountRequest request){
        CreateAccountResponse response = accountUseCase.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping
    public ResponseEntity<GetAllAccountsResponse> getAllAccounts(){
        GetAllAccountsResponse response = accountUseCase.getAllAccounts();
        return ResponseEntity.ok(response);
    }
    @IsAuthenticated
    @RolesAllowed({"RegularUser","Admin"})
    @PatchMapping("/details/{accountId}")
    public ResponseEntity<UpdateUserInfoResponse> updateAccount(
            @PathVariable Long accountId,
            @RequestBody UpdateUserInfoRequest request,
            HttpServletRequest httpServletRequest) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        UpdateUserInfoResponse response = accountUseCase.updateUserInfo(accountId, request, token);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/Signin")
    public ResponseEntity<SignInResponse> signIn(@RequestBody @Valid SignInRequest request){
        SignInResponse response = accountUseCase.signIn(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/accountname")
    public ResponseEntity<GetAccountByAccountNameResponse> getAccountByAccountName(@RequestParam String accountName){
        GetAccountByAccountNameResponse response = accountUseCase.getAccountByAccountName(GetAccountByAccountNameRequest.builder().accountName(accountName).build());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
