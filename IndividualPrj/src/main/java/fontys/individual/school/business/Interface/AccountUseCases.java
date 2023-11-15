package fontys.individual.school.business.Interface;

import fontys.individual.school.domain.HttpResponseRequest.GetAllAccountsResponse;
import fontys.individual.school.domain.HttpResponseRequest.*;


public interface AccountUseCases {
CreateAccountResponse createAccount(CreateAccountRequest request);
GetAllAccountsResponse getAllAccounts();
SignInResponse signIn(SignInRequest request);
GetAccountByAccountNameResponse getAccountByAccountName(GetAccountByAccountNameRequest request);
UpdateUserInfoResponse updateUserInfo(Long userId,UpdateUserInfoRequest request, String token);
}
