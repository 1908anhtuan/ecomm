package fontys.individual.school.business.impl.Converter;

import fontys.individual.school.domain.Delivery;
import fontys.individual.school.persistence.entity.DeliveryEntity;

public interface DeliveryConverter <E extends DeliveryEntity, A extends Delivery>{
    A convert(E entity);
    E convertToEntity(A obj);
}
