package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.enumClasses.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceRequest {
    private String description;
    @Min(value = 0)
    private double fee;
    @NotNull
    private ServiceType serviceType;
}
