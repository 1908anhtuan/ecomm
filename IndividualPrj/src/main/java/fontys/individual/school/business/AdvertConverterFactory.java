package fontys.individual.school.business;

import fontys.individual.school.business.impl.Converter.AdvertConverter;
import fontys.individual.school.business.impl.Converter.BiddingAdvertConverter;
import fontys.individual.school.business.impl.Converter.RegularAdvertConverter;
import fontys.individual.school.persistence.entity.BiddingAdEntity;
import fontys.individual.school.persistence.entity.DigitalAdvertEntity;
import fontys.individual.school.persistence.entity.RegularAdEntity;
import org.springframework.stereotype.Component;

@Component
public class AdvertConverterFactory {
    private final RegularAdvertConverter regularAdvertConverter;
    private final BiddingAdvertConverter biddingAdvertConverter;

    public AdvertConverterFactory(RegularAdvertConverter regularAdvertConverter, BiddingAdvertConverter biddingAdvertConverter ) {
        this.regularAdvertConverter = regularAdvertConverter;
        this.biddingAdvertConverter = biddingAdvertConverter;
    }

    public AdvertConverter getConverter(DigitalAdvertEntity entity) {
        if(entity instanceof RegularAdEntity) {
            return regularAdvertConverter;
        } else if(entity instanceof BiddingAdEntity) {
            return biddingAdvertConverter;
        } else {
            throw new IllegalArgumentException("Invalid entity type");
        }
    }
}
