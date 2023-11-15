package fontys.individual.school.persistence;

import fontys.individual.school.domain.enumClasses.DeliveryStatus;
import fontys.individual.school.persistence.entity.HomeDeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Repository
public interface HomeDeliveryRepository extends JpaRepository<HomeDeliveryEntity, Long> {
    @Query("SELECT COUNT(h) FROM HomeDeliveryEntity h " +
            "WHERE h.deliveryDate >= :startDate AND h.deliveryDate <= :endDate ")
    long getHomeDeliveryCount(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Transactional
    @Modifying
    @Query("UPDATE HomeDeliveryEntity h SET h.deliveryStatus = :status WHERE h.id = :id")
    int updateDeliveryStatus(@Param("id") Long id, @Param("status") DeliveryStatus status);
}
