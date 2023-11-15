package fontys.individual.school.domain.HttpResponseRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {
    @NotNull(message = "account name is required")
    @NotBlank(message = "account name cant be blank")
    private String accountName;

    @NotNull(message = "password is required")
    @NotBlank(message = "password cant be blank")
    private String password;


}
