package fontys.individual.school.domain.HttpResponseRequest;

import fontys.individual.school.domain.enumClasses.ProductCondition;
import fontys.individual.school.persistence.entity.AccountEntity;
import fontys.individual.school.persistence.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBiddingAdvertRequest {
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

    @NotNull
    private ProductCondition condition;

    @Builder.Default private double lowestBidAmountAllowed = 1;

    @Builder.Default private double initialBidPrice = 1;
    @Builder.Default private double binPrice = 10;
    @NotNull
    private LocalDateTime bidExpirationDate;

}
