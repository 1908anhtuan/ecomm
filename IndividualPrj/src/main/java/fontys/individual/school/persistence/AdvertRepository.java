package fontys.individual.school.persistence;

import fontys.individual.school.persistence.entity.DigitalAdvertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertRepository extends JpaRepository<DigitalAdvertEntity, Long> {

}
