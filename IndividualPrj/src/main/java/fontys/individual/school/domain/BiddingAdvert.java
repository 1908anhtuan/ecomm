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

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BiddingAdvert extends DigitalAdvert {
    private double lowestBidAmountAllowed;
    @Builder.Default private double currentHighestBid = 0;
    private double initialBidPrice;
    private double binPrice;
    private LocalDateTime bidExpirationDate;
    @JsonSerialize(using = OptionalSerializer.class)
    private Optional<Account> highestBidder;
    public BiddingAdvert(Long id, String title, String productDescription, Category category, double lowestBidAmountAllowed, ProductCondition condition, double binPrice, LocalDateTime bidExpirationDate, Account advertiser, double initialBidPrice){
        super(id, title, productDescription, category, advertiser, condition);
        this.lowestBidAmountAllowed = lowestBidAmountAllowed;
        this.initialBidPrice = initialBidPrice;
        this.binPrice = binPrice;
        this.bidExpirationDate = bidExpirationDate;
    }


    @Override
    public AdvertType getAdvertType() {
        return AdvertType.Bidding;
    }

    @Override
    public Optional<Account> getBuyer() {

        return highestBidder;
    }



    @Override
    public double calculateTotalPrice() {
        Optional<Delivery> deliveryType = getDeliveryType();
        if (deliveryType.isPresent()) {
            return this.currentHighestBid + deliveryType.get().getDeliveryFee();
        } else {
            return this.currentHighestBid;
        }
    }
    @Override
    public boolean buyInstantly(Account buyer){
        if(isBidExpired()){
            return false;
        }

        setEndStatus(true);
        setSoldStatus(true);
        setAdStatus(AdStatus.Unavailable);
        this.highestBidder = Optional.of(buyer);
        return true;
    }
    public boolean placeBid(double bid, Account bidder){
        if(isBidExpired()){
            return false;
        }
        if(bid > binPrice){
            this.currentHighestBid = binPrice;
            this.highestBidder = Optional.of(bidder);
            setEndStatus(true);
            setSoldStatus(true);
            setAdStatus(AdStatus.Unavailable);
            return true;
        }
        if(!(bid > currentHighestBid + lowestBidAmountAllowed)){
            return false;
        }
        this.currentHighestBid = bid;
        this.highestBidder = Optional.of(bidder);
        return true;
    }
    public double getHighestBid(){
        return currentHighestBid;
    }
    public boolean isBidExpired(){
        if(isEnded()){
            return true;
        }
        LocalDateTime now = LocalDateTime.now();
        if(now.isAfter(bidExpirationDate)){
            setEndStatus(true);
            setSoldStatus(true);
            return true;
        }else{
            return false;
        }
    }
    public boolean end(){
        if(!isEnded()){
            setEndStatus(true);
            setSoldStatus(true);
            return true;
        }else{
            return false;
        }
    }
}
