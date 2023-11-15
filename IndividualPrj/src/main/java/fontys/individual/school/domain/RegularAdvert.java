package fontys.individual.school.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fontys.individual.school.business.impl.Converter.OptionalSerializer;
import fontys.individual.school.domain.enumClasses.AdStatus;
import fontys.individual.school.domain.enumClasses.AdvertType;
import fontys.individual.school.domain.enumClasses.ProductCondition;
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
public class RegularAdvert extends DigitalAdvert {
    @JsonSerialize(using = OptionalSerializer.class)
    @Builder.Default private Optional<Account> buyer = Optional.empty();
    private double price;
    public RegularAdvert(Long id, String title, String productDescription, Category category, Account advertiser, ProductCondition condition, double price){
        super(id, title, productDescription, category, advertiser, condition);
        this.price = price;
    }
    @Override
    public double calculateTotalPrice() {
        double totalPrice = this.price + getDeliveryType().get().getDeliveryFee();
        return totalPrice;
    }
    @Override
    public Optional<Account> getBuyer() {
        return buyer;
    }
    @Override
    public boolean buyInstantly(Account buyer) {
        if(isSold()){
            return false;
        }
        this.buyer = Optional.of(buyer);
        setSoldStatus(true);
        setEndStatus(true);
        setAdStatus(AdStatus.Unavailable);
        return true;
    }

    public boolean changePrice(double newPrice){
        if(isSold() || isEnded()){
            return false;
        }
        if(newPrice <= 0){
            return false;
        }
        this.price = newPrice;
        return true;
    }

    public boolean cancelAd(){
        if(isSold() || isEnded()){
            return false;
        }
        setEndStatus(true);
        return true;

    }
    @Override
    public AdvertType getAdvertType() {
        return AdvertType.Regular;
    }
}
