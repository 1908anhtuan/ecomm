package fontys.individual.school.domain;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fontys.individual.school.business.impl.Converter.OptionalSerializer;
import fontys.individual.school.domain.enumClasses.AdStatus;
import fontys.individual.school.domain.enumClasses.AdvertType;
import fontys.individual.school.domain.enumClasses.ProductCondition;
import fontys.individual.school.domain.enumClasses.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class DigitalAdvert {
private Long id;
private String title;
private String productDescription;
@Builder.Default private int viewCount = 0;
private Category category;
private Account advertiser;
private ProductCondition condition;
@Builder.Default private boolean isSold = false;
@Builder.Default private boolean isEnded = false;
@Builder.Default private VerificationStatus verification = VerificationStatus.Pending;
@Builder.Default private AdStatus adStatus = AdStatus.Unavailable;
@JsonSerialize(using = OptionalSerializer.class)
@Builder.Default private Optional<Delivery> deliveryType = Optional.empty();
public DigitalAdvert(Long id, String title, String productDescription, Category category, Account advertiser, ProductCondition condition){
    this.id = id;
    this.title = title;
    this.productDescription = productDescription;
    this.category = category;
    this.advertiser = advertiser;
    this.condition = condition;
}
public  abstract Optional<Account> getBuyer();
public void setEndStatus(boolean status){
    this.isEnded = status;
}
public abstract double calculateTotalPrice();
public void setSoldStatus(boolean status){
    this.isSold = status;
}
public abstract boolean buyInstantly(Account buyer);
public abstract AdvertType getAdvertType();
public void setAdStatus(AdStatus adStatus){
    this.adStatus = adStatus;
}
public void setDeliveryType(Delivery deliveryType){
    this.deliveryType = Optional.of(deliveryType);
}
}
