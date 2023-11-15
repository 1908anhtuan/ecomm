package fontys.individual.school.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private long id;
    private String accountName;
    private String password;
    private double balance ;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

}
