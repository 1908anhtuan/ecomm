package fontys.individual.school.business.impl.Converter;

import fontys.individual.school.domain.Account;
import fontys.individual.school.persistence.entity.AccountEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AccountConverter {


    public static Account convertToAccount(AccountEntity account){
        if(account == null){
            return null;
        }
        return Account.builder()
                .id(account.getId())
                .accountName(account.getAccountName())
                .password(account.getPassword())
                .balance(account.getBalance())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .phoneNumber(account.getPhoneNumber())
                .email(account.getEmail())
                .build();
    }

    public static AccountEntity convertToAccountEntity(Account account) {
        if(account == null){
            return null;
        }

        return AccountEntity.builder()
                .id(account.getId())
                .accountName(account.getAccountName())
                .password(account.getPassword())
                .balance(account.getBalance())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .phoneNumber(account.getPhoneNumber())
                .email(account.getEmail())
                .build();
    }
}
