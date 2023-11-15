package fontys.individual.school.business.impl.Converter;

import fontys.individual.school.domain.DigitalAdvert;
import fontys.individual.school.persistence.entity.DigitalAdvertEntity;

public interface AdvertConverter<E extends DigitalAdvertEntity, A extends DigitalAdvert> {
    A convert(E entity);
    E convertToEntity(A obj);
}