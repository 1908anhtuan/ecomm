package fontys.individual.school.business.impl.Converter;

import fontys.individual.school.business.impl.Converter.AdvertConverter;
import fontys.individual.school.domain.RegularAdvert;
import fontys.individual.school.persistence.entity.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RegularAdvertConverter implements AdvertConverter<RegularAdEntity, RegularAdvert> {
    @Override
    public RegularAdvert convert(RegularAdEntity entity) {
        if(entity == null){
            return null;
        }
        RegularAdvert regularAdvert = RegularAdvert.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .productDescription(entity.getProductDescription())
                .category(CategoryConverter.convertToCategory(entity.getCategory()))
                .verification(entity.getVerification())
                .adStatus(entity.getAdStatus())
                .isSold(entity.isSold())
                .advertiser(AccountConverter.convertToAccount(entity.getAdvertiser()))
                .condition(entity.getCondition())
                .price(entity.getPrice())
                .viewCount(entity.getViewCount())
                .buyer(entity.getBuyer() != null ? Optional.of(AccountConverter.convertToAccount(entity.getBuyer())) : Optional.empty()).build();
        if(entity.getDeliveryType() != null)
        {
            DeliveryEntity deliveryOption = entity.getDeliveryType();
            if (deliveryOption instanceof PickUpEntity) {
                PickUpEntity pickUpEntity = (PickUpEntity) deliveryOption;
                regularAdvert.setDeliveryType(new PickUpDeliveryConverter().convert(pickUpEntity));
            }else{
                HomeDeliveryEntity homeEntity = (HomeDeliveryEntity)deliveryOption;
                regularAdvert.setDeliveryType(new HomeDeliveryConverter().convert(homeEntity));
            }
        }
        return regularAdvert;
    }

    @Override
    public RegularAdEntity convertToEntity(RegularAdvert advert) {
        if(advert == null){
            return null;
        }
        RegularAdEntity entity = RegularAdEntity.builder()
                .id(advert.getId())
                .title(advert.getTitle())
                .productDescription(advert.getProductDescription())
                .category(CategoryConverter.convertToCategoryEntity(advert.getCategory()))
                .verification(advert.getVerification())
                .condition(advert.getCondition())
                .adStatus(advert.getAdStatus())
                .isSold(advert.isSold())
                .isEnded(advert.isEnded())
                .advertiser(AccountConverter.convertToAccountEntity(advert.getAdvertiser()))
                .price(advert.getPrice())
                .buyer(advert.getBuyer().map(AccountConverter::convertToAccountEntity).orElse(null))
                .build();
        return entity;
        }
    }

