package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.enumClasses.ProductCondition;
import fontys.individual.school.persistence.entity.AccountEntity;
import fontys.individual.school.persistence.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRegularAdvertRequest {
    @NotBlank
    @NotNull
    private String title;
    @NotNull
    @NotBlank
    private String productDescription;
    @NotNull
    private CategoryEntity category;
    @NotNull
    private AccountEntity advertiser;
    @Min(value = 1)
    @NotNull
    private double price;

    @NotNull
    private ProductCondition condition;
}
