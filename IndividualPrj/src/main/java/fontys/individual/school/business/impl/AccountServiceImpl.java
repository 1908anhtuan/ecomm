package fontys.individual.school.business.impl;

import fontys.individual.school.business.Interface.AccessTokenDecoder;
import fontys.individual.school.business.Interface.AccessTokenEncoder;
import fontys.individual.school.business.Interface.AccountUseCases;
import fontys.individual.school.business.exception.AccountAlreadyExistsException;
import fontys.individual.school.business.exception.ForbiddenException;
import fontys.individual.school.business.exception.InvalidCredentialsException;
import fontys.individual.school.business.exception.UserNotFoundException;
import fontys.individual.school.business.impl.Converter.AccountConverter;
import fontys.individual.school.domain.*;

import fontys.individual.school.domain.HttpResponseRequest.GetAllAccountsResponse;
import fontys.individual.school.domain.HttpResponseRequest.*;

import fontys.individual.school.domain.enumClasses.AccountType;
import fontys.individual.school.persistence.AccountRepository;
import fontys.individual.school.persistence.RoleRepository;
import fontys.individual.school.persistence.entity.AccountEntity;
import fontys.individual.school.persistence.entity.RoleEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountUseCases {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;
    private final AccessTokenDecoder accessTokenDecoder;
    private final RoleRepository roleRepository;

    @Override
    public CreateAccountResponse createAccount(CreateAccountRequest request){
        if(accountRepository.existsByAccountName(request.getAccountName())){
            throw new AccountAlreadyExistsException();
        }


        AccountEntity savedAccount = saveNewAccount(request);

        roleRepository.save(RoleEntity.builder().user(savedAccount).role(AccountType.RegularUser).build());
        return CreateAccountResponse.builder()
                .accountId(savedAccount.getId())
                .build();
    }



    @Override
    public GetAllAccountsResponse getAllAccounts() {
        List<AccountEntity> results = accountRepository.findAll();


        List<Account> accounts = results
                .stream()
                .map(AccountConverter::convertToAccount)
                .toList();



        return GetAllAccountsResponse.builder()
                .accounts(accounts)
                .build();
    }



    @Override
    public SignInResponse signIn(SignInRequest request) {
        Optional<AccountEntity> accountEntity = accountRepository.findByAccountName(request.getAccountName());

        if(!accountEntity.isPresent()){
            throw new UserNotFoundException();
        }
        if (!matchesPassword(request.getPassword(), accountEntity.get().getPassword())) {
            throw new InvalidCredentialsException();
        }
        String accessToken = generateAccessToken(accountEntity.get());
        return SignInResponse.builder().accessToken(accessToken).build();

    }

    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public GetAccountByAccountNameResponse getAccountByAccountName(GetAccountByAccountNameRequest request) {
        Optional<AccountEntity> accountEntity = accountRepository.findByAccountName(request.getAccountName());
        if(!accountEntity.isPresent()){
            throw new UserNotFoundException();
        }
        Account account = AccountConverter.convertToAccount(accountEntity.get());
        return GetAccountByAccountNameResponse.builder().account(account).build();
    }

    @Override
    public UpdateUserInfoResponse updateUserInfo(Long id,UpdateUserInfoRequest request, String token) {
        String message;
        AccessToken accessToken = accessTokenDecoder.decode(token);
        if(!accessToken.getAccountId().equals(id)){
            throw new ForbiddenException();
        }
        boolean isUpdated = false;
        int affectedRow = accountRepository.updateUserInfo(id, request.getFirstName(), request.getLastName(), request.getPhoneNumber(), request.getEmail());
        if(affectedRow > 0){
            isUpdated = true;
        }

        if(!isUpdated){
            message = "Fail to update account!";
        }else{
            message = "Successfully updated!";
        }
        return UpdateUserInfoResponse.builder().message(message).build();
    }


    private AccountEntity saveNewAccount(CreateAccountRequest request){
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        AccountEntity newAccount = AccountEntity.builder()
                .accountName(request.getAccountName())
                .password(encodedPassword)
                .balance(10000.0)
                .build();

        return accountRepository.save(newAccount);
    }
    private String generateAccessToken(AccountEntity account) {
        List<String> roles = account.getUserRoles().stream()
                .map(userRole -> userRole.getRole().toString())
                .toList();


        return accessTokenEncoder.encode(
                AccessToken.builder()
                        .subject(account.getAccountName())
                        .roles(roles)
                        .accountId(account.getId())
                        .build());
    }
}
