package fontys.individual.school.business.impl.Converter;

import fontys.individual.school.domain.Address;
import fontys.individual.school.domain.PickUpFacility;
import fontys.individual.school.persistence.entity.AddressEntity;
import fontys.individual.school.persistence.entity.FacilityEntity;
import lombok.NoArgsConstructor;

import java.util.Optional;
@NoArgsConstructor
public class FacilityConverter {
    public static PickUpFacility convertToFacility(FacilityEntity entity){
        PickUpFacility facility = PickUpFacility.builder()
                .id(entity.getId())
                .facilityName(entity.getName())
                .city(entity.getCity())
                .postCode(entity.getPostCode())
                .openTime(entity.getOpenTime())
                .closeTime(entity.getCloseTime())
                .build();

        return facility;

    }
    public static FacilityEntity convertToFacilityEntity(PickUpFacility facility) {
        FacilityEntity entity = FacilityEntity.builder()
                .id(facility.getId())
                .name(facility.getFacilityName())
                .city(facility.getCity())
                .postCode(facility.getPostCode())
                .openTime(facility.getOpenTime())
                .closeTime(facility.getCloseTime())
                .build();

        return entity;
    }
}
