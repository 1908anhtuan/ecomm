package fontys.individual.school.business.impl.Converter;

import fontys.individual.school.business.impl.Converter.AdvertConverter;
import fontys.individual.school.domain.BiddingAdvert;
import fontys.individual.school.domain.Delivery;
import fontys.individual.school.domain.PickUpDelivery;
import fontys.individual.school.persistence.entity.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class BiddingAdvertConverter implements AdvertConverter<BiddingAdEntity, BiddingAdvert> {
    @Override
    public BiddingAdvert convert(BiddingAdEntity entity) {
        if(entity == null){
            return null;
        }
        BiddingAdvert biddingAdvert =  BiddingAdvert.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .productDescription(entity.getProductDescription())
                .category(CategoryConverter.convertToCategory(entity.getCategory()))
                .advertiser(AccountConverter.convertToAccount(entity.getAdvertiser()))
                .lowestBidAmountAllowed(entity.getLowestBidAmountAllowed())
                .currentHighestBid(entity.getCurrentHighestBid())
                .initialBidPrice(entity.getInitialBidPrice())
                .binPrice(entity.getBinPrice())
                .bidExpirationDate(entity.getBidExpirationDate())
                .verification(entity.getVerification())
                .isSold(entity.isSold())
                .condition(entity.getCondition())
                .isEnded(entity.isEnded())
                .adStatus(entity.getAdStatus())
                .viewCount(entity.getViewCount())
                .build();
        if(entity.getHighestBidder() != null){
            biddingAdvert.setHighestBidder(Optional.of(AccountConverter.convertToAccount(entity.getHighestBidder())));
        }
        if(entity.getDeliveryType() != null)
        {
            DeliveryEntity deliveryOption = entity.getDeliveryType();
            if (deliveryOption instanceof PickUpEntity) {
                PickUpEntity pickUpEntity = (PickUpEntity) deliveryOption;
                biddingAdvert.setDeliveryType(new PickUpDeliveryConverter().convert(pickUpEntity));
            }else{
                HomeDeliveryEntity homeEntity = (HomeDeliveryEntity)deliveryOption;
                biddingAdvert.setDeliveryType(new HomeDeliveryConverter().convert(homeEntity));
            }
        }

        return biddingAdvert;
    }

    @Override
    public BiddingAdEntity convertToEntity(BiddingAdvert biddingAdvert) {
        if(biddingAdvert == null){
            return null;
        }
        return BiddingAdEntity.builder()
                .id(biddingAdvert.getId())
                .title(biddingAdvert.getTitle())
                .productDescription(biddingAdvert.getProductDescription())
                .category(CategoryConverter.convertToCategoryEntity(biddingAdvert.getCategory()))
                .advertiser(AccountConverter.convertToAccountEntity(biddingAdvert.getAdvertiser()))
                .lowestBidAmountAllowed(biddingAdvert.getLowestBidAmountAllowed())
                .currentHighestBid(biddingAdvert.getCurrentHighestBid())
                .initialBidPrice(biddingAdvert.getInitialBidPrice())
                .binPrice(biddingAdvert.getBinPrice())
                .condition(biddingAdvert.getCondition())
                .isEnded(biddingAdvert.isEnded())
                .bidExpirationDate(biddingAdvert.getBidExpirationDate())
                .highestBidder(biddingAdvert.getHighestBidder().map(AccountConverter::convertToAccountEntity).orElse(null))
                .verification(biddingAdvert.getVerification())
                .adStatus(biddingAdvert.getAdStatus())
                .build();
    }
}