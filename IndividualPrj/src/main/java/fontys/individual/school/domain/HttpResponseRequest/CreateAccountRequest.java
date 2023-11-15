package fontys.individual.school.domain.HttpResponseRequest;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {

    @NotNull
    @NotBlank
    private String accountName;
    @NotNull
    @NotBlank
    private String password;

}
