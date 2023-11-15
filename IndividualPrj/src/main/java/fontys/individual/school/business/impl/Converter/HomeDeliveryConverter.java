package fontys.individual.school.business.impl.Converter;

import fontys.individual.school.domain.Delivery;
import fontys.individual.school.domain.HomeDelivery;
import fontys.individual.school.domain.PickUpDelivery;
import fontys.individual.school.persistence.entity.DeliveryEntity;
import fontys.individual.school.persistence.entity.HomeDeliveryEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class HomeDeliveryConverter implements DeliveryConverter<HomeDeliveryEntity, HomeDelivery>{

    @Override
    public HomeDelivery convert(HomeDeliveryEntity entity) {
        return HomeDelivery.builder()
                .id(entity.getId())
                .deliveryDate(Optional.ofNullable(entity.getDeliveryDate()))
                .startTime(Optional.ofNullable(entity.getStart_time()))
                .endTime(Optional.ofNullable(entity.getEnd_time()))
                .deliveryFee(entity.getDeliveryFee())
                .deliveryStatus(entity.getDeliveryStatus())
                .postCode(entity.getPostCode())
                .houseNumber(entity.getHouseNumber())
                .deliveryDescription(entity.getDeliveryDescription())
                .city(entity.getCity())
                .street(entity.getStreet())
                .build();
    }

    @Override
    public HomeDeliveryEntity convertToEntity(HomeDelivery obj) {
        return HomeDeliveryEntity.builder()
                .id(obj.getId())
                .deliveryDate(obj.getDeliveryDate().orElse(null))
                .start_time(obj.getStartTime().orElse(null))
                .end_time(obj.getEndTime().orElse(null))
                .deliveryFee(obj.getDeliveryFee())
                .deliveryStatus(obj.getDeliveryStatus())
                .postCode(obj.getPostCode())
                .houseNumber(obj.getHouseNumber())
                .deliveryDescription(obj.getDeliveryDescription())
                .city(obj.getCity())
                .street(obj.getStreet())
                .build();
    }
}
