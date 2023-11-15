package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.enumClasses.DeliveryStatus;
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
public class UpdatePickUpDeliveryRequest {
    @NotBlank
    private Long id;
    @NotNull
    private DeliveryStatus deliveryStatus;
}
