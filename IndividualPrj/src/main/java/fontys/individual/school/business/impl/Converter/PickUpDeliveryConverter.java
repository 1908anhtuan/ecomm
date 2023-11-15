package fontys.individual.school.business.impl.Converter;


import fontys.individual.school.domain.PickUpDelivery;

import fontys.individual.school.persistence.entity.DeliveryEntity;
import fontys.individual.school.persistence.entity.HomeDeliveryEntity;
import fontys.individual.school.persistence.entity.PickUpEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PickUpDeliveryConverter implements DeliveryConverter<PickUpEntity, PickUpDelivery>{

    @Override
    public PickUpDelivery convert(PickUpEntity entity) {
        return PickUpDelivery.builder()

                .id(entity.getId())
                .deliveryDate(Optional.ofNullable(entity.getDeliveryDate()))
                .startTime(Optional.ofNullable(entity.getStart_time()))
                .endTime(Optional.ofNullable(entity.getEnd_time()))
                .deliveryFee(entity.getDeliveryFee())
                .deliveryStatus(entity.getDeliveryStatus())
                .pickUpFacility(Optional.of(FacilityConverter.convertToFacility(entity.getFacility())))
                .build();


    }

    @Override
    public PickUpEntity convertToEntity(PickUpDelivery obj) {
        return PickUpEntity.builder()
                .id(obj.getId())
                .deliveryDate(obj.getDeliveryDate().orElse(null))
                .start_time(obj.getStartTime().orElse(null))
                .end_time(obj.getEndTime().orElse(null))
                .deliveryFee(obj.getDeliveryFee())
                .deliveryStatus(obj.getDeliveryStatus())
                .facility(obj.getPickUpFacility().map(FacilityConverter::convertToFacilityEntity).orElse(null))
                .build();
    }


}
